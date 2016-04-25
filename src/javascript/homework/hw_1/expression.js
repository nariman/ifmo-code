/**
 * Nariman Safiulin (woofilee)
 * File: expression.js
 * Created on: Apr 24, 2016
 */

"use strict";

var expressionObject = function (f) {
    return function (x, y, z) {
        return f(x, y, z);
    }
};

var binaryOperation = function (f) {
    return function (left, right) {
        return expressionObject(function (x, y, z) {
            return f(left(x, y, z), right(x, y, z));
        })
    }
};

var unaryOperation = function (f) {
    return function (value) {
        return expressionObject(function (x, y, z) {
            return f(value(x, y, z));
        })
    }
};

var cnst = function (value) {
    return expressionObject(function (x, y, z) {
        return value;
    })
};

var variable = function (name) {
    if (name.length > 1) {
        throw new Error("Variable must be one of 'x', 'y', 'z'!")
    }
    var index = "xyz".indexOf(name);
    return expressionObject(function (x, y, z) {
        return arguments[index];
    })
};

var abs = unaryOperation(Math.abs);
var add = binaryOperation(function (left, right) {
    return left + right;
});
var divide = binaryOperation(function (left, right) {
    return left / right;
});
var log = unaryOperation(Math.log);
var mod = binaryOperation(function (left, right) {
    return left % right;
});
var multiply = binaryOperation(function (left, right) {
    return left * right;
});
var negate = unaryOperation(function (value) {
    return -value;
});
var power = binaryOperation(Math.pow);
var subtract = binaryOperation(function (left, right) {
    return left - right;
});

var square = function (value) {
    return power(value, cnst(2));
};

var parse = function (expression) {
    // println(expression);

    var elements = expression.split(" ").filter(function (element, index, elements) {
        return element.length > 0;
    });
    var stack = [];

    var binaryAction = function (f) {
        if (stack.size < 1) {
            throw new SyntaxError("Cannot to parse this Reverse Polish notation expression due RPn expression contain mistake :(");
        }

        var second = stack.pop();
        var first = stack.pop();
        stack.push(f(first, second));
    };

    var unaryAction = function (f) {
        if (stack.size < 1) {
            throw new SyntaxError("Cannot to parse this Reverse Polish notation expression due RPn expression contain mistake :(");
        }

        stack.push(f(stack.pop()));
    };

    elements.forEach(function (element, index, elements) {
        switch (element) {
            case "+":
                binaryAction(add);
                break;
            case "-":
                binaryAction(subtract);
                break;
            case "*":
                binaryAction(multiply);
                break;
            case "/":
                binaryAction(divide);
                break;
            case "**":
                binaryAction(power);
                break;
            case "%":
                binaryAction(mod);
                break;
            case "abs":
                unaryAction(abs);
                break;
            case "log":
                unaryAction(log);
                break;
            case "negate":
                unaryAction(negate);
                break;
            default:
                if (element.match(/^[0-9\.\-]+$/)) {
                    stack.push(cnst(parseInt(element)));
                } else {
                    stack.push(variable(element));
                }
        }
    });

    if (stack.size > 1) {
        throw new SyntaxError("Cannot to parse this Reverse Polish notation expression due RPn expression contain a mistake :(");
    }

    return stack.pop();
};

// parse("x x 2 - * x * 1 +");

// for (var i = 0; i < 11; i++) {
//     add(subtract(square(variable('x')), multiply(cnst(2), variable('x'))), cnst(1))(i);
// }