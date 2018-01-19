(function() {
  const API_URL = "http://localhost:3000";

  new Vue({
    el: "#editor",
    data: {
      input: "# Hello",
      currentId: null,
      files: []
    },
    created: function() {
      this.$http.get(API_URL, {
        markdown: this.input
      })
        .then(response => {
          this.files = response.body;
          console.log("File list loaded");
        })
        .catch(error => {
          console.error(error);
        });
    },
    computed: {
      renderedMarkdown: function () {
        return marked(this.input, { sanitize: true })
      }
    },
    methods: {
      update: _.debounce(function (e) {
        this.input = e.target.value
      }, 300),

      create: function() {
        this.currentId = null;
        this.input = "";
      },

      clear: function() {
        this.input = "";
      },

      open: function(file) {
        this.currentId = file._id;
        this.input = file.markdown;
      },

      save: function() {
        if (!this.currentId) {
          this.$http.post(API_URL, {
            markdown: this.input
          })
            .then(response => {
              this.currentId = response.body["ops"][0]["_id"];
              this.files.unshift(response.body["ops"][0]);
              console.log("File successfully saved!");
            })
            .catch(error => {
              console.error(error);
            });
        } else {
          this.$http.put(API_URL + "/" + this.currentId, {
            markdown: this.input
          })
            .then(response => {
              console.log("File successfully saved (updated)!");
            })
            .catch(error => {
              console.error(error);
            });
        }
      }
    }
  })
})();
