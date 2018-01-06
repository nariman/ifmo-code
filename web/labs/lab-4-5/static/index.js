//
// 500px API

let _500PX_API_URL = 'https://api.500px.com/v1/'
// This key will be revoked, don't try to use it
let _500PX_CONSUMER_KEY = 'rS8W6ZPmkDbAa7Ctgi3Yhv6DyZi3lAQh9vtEtFQd'

function construct_500px_request(path, data, callback) {
    data.consumer_key = _500PX_CONSUMER_KEY;
    construct_jsonp_request(_500PX_API_URL + path + '.jsonp', data, callback);
}

function get_random_photo(callback) {
    construct_500px_request('photos', {
        feature: 'popular'
    }, (data) => {
        let photo_id = data['photos'][get_random_integer(0, data['photos'].length)]['id'];
        construct_500px_request('photos' + '/' + photo_id, {}, (data) => {
            callback(data['photo']);
        });
    });
}

//
// Quotes on Design

let _QUOTES_ON_DESIGN_API_URL = 'https://quotesondesign.com/wp-json/'

function construct_quotes_on_design_request(path, data, callback) {
    construct_jsonp_request(_QUOTES_ON_DESIGN_API_URL + path, data, callback, '_jsonp');
}

function get_random_quote(callback) {
    construct_quotes_on_design_request('posts', {
        filter: {
            orderby: 'rand',
            posts_per_page: 1
        }
    }, (data) => {
        callback(data[get_random_integer(0, data.length)])
    });
}

//
// Page logic

let _SAVING_API_URL = window.location.origin + '/';

(function() {
    let generate_btn = document.getElementById('generate-btn');
    let save_btn = document.getElementById('save-btn');
    generate_btn.addEventListener('click', generate_new_photo_quote);
    save_btn.addEventListener('click', save_photo_quote);
    generate_new_photo_quote();
})();

function generate_new_photo_quote() {
    let canvas = document.getElementById('canvas');
    let ctx = canvas.getContext('2d');
    let generate_btn = document.getElementById('generate-btn');
    let save_btn = document.getElementById('save-btn');

    generate_btn.disabled = save_btn.disabled = true;
    console.log('Generation requested');

    console.log('Requesting random photo');
    get_random_photo((photo) => {
        console.log('Random photo received id #' + photo['id']);

        console.log('Requesting random quote');
        get_random_quote((quote) => {
            console.log('Random quote received, id #' + quote['ID']);

            let img = document.createElement('img');
            img.src = 'https://cors-anywhere.herokuapp.com/' + photo['image_url'];
            img.crossOrigin = 'anonymous';

            let p = document.createElement('img');
            p.innerHTML = quote['content'];

            img.style = p.style = 'display: none';

            document.body.appendChild(img);
            document.body.appendChild(p);

            img.onload = function() {
                canvas.width = img.width;
                canvas.height = img.height;
                ctx.drawImage(img, 0, 0);

                ctx.fillStyle = 'rgba(0, 0, 0, 0.6)';
                ctx.fillRect(0, 0, canvas.width, canvas.height);

                ctx.font = '32px Arial';
                ctx.fillStyle = '#fff';

                let words = p.textContent.trim().split(' ');
                let lines = [];
                let quote_formatted = '';

                for (let i = 0; i < words.length; i++) {
                    if (ctx.measureText(quote_formatted + ' ' + words[i]).width > canvas.width - canvas.width * 0.1) {
                        lines.push(quote_formatted);
                        quote_formatted = words[i];
                    } else {
                        quote_formatted = quote_formatted + ' ' + words[i];
                    }
                }

                lines.push(quote_formatted);
                let margin_top = Math.floor(canvas.height / 2 - (32 + 12) * lines.length / 2);

                for (let i = 0; i < lines.length; i++) {
                    margin_top += 6;
                    let margin_left = Math.floor(canvas.width / 2 - ctx.measureText(lines[i]).width / 2);
                    ctx.fillText(lines[i], margin_left, margin_top);
                    margin_top += 32 + 6;
                }

                document.body.removeChild(img);
                document.body.removeChild(p);
                generate_btn.disabled = save_btn.disabled = false;
            };
        })
    });
}

function save_photo_quote() {
    let canvas = document.getElementById('canvas');
    let save_btn = document.getElementById('save-btn');
    let photo_quote_saved_url_input = document.getElementById('photo-quote-saved-url-input');

    save_btn.disabled = true;
    console.log('Saving requested');

    canvas.toBlob((blob) => {
        let form_data = new FormData()
        form_data.append('blob', blob, 'photo');

        fetch(_SAVING_API_URL, {
            method: 'POST',
            body: form_data
        }).then(function(response) {
            return response.json();
        }).then(function(data) {
            console.log('Saved, id #' + data['id']);
            photo_quote_saved_url_input.value = _SAVING_API_URL + 'photo/' + data['id'] + '.png';
            save_btn.disabled = false;
        }).catch(function(error) {
            console.log(error.message);
            save_btn.disabled = false;
        })
    });
}

//
// Helpers

function get_random_integer(min, max) {
  return Math.floor(Math.random() * (max - min)) + min;
}

function construct_jsonp_request(url, data, callback, callback_param_name) {
    let callback_name = '_' + random_callback_name();
    let tag = document.createElement('script');

    data[callback_param_name ? callback_param_name : 'callback'] = callback_name;
    tag.src = url + '?' + object_to_params(data);

    window[callback_name] = function (data) {
        document.body.removeChild(tag);
        callback(data);
    };

    document.body.appendChild(tag);
}

function random_callback_name() {
    return 'callback_' + String(Math.round(Math.random() * 100000000));
};

function encode_param_name(name, root) {
    if (root) {
        return encodeURIComponent(root + '[' + name + ']');
    } else {
        return encodeURIComponent(name);
    }
};

function object_to_params(object, root) {
    let string_parts = [], property, i;

    for (property in object) {
        if (object.hasOwnProperty(property)) {
            let value = object[property];

            if (value instanceof Array) {
                for (i = 0; i < value.length; i++) {
                    let encoded_value = encodeURIComponent(value[i]);
                    string_parts.push(encode_param_name(property, root) + '%5B%5D=' + encoded_value);
                }
            } else if (typeof value == 'object') {
                string_parts.push(object_to_params(value, encode_param_name(property, root)));
            } else {
                string_parts.push(encode_param_name(property, root) + '=' + encodeURIComponent(value));
            }
        }
    }

    return string_parts.join('&');
};
