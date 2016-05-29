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

var unaryOperation = function (f) {
    return function (expression) {
        return expressionObject(function (x, y, z) {
            return f(expression(x, y, z));
        })
    }
};

var binaryOperation = function (f) {
    return function (left, right) {
        return expressionObject(function (x, y, z) {
            return f(left(x, y, z), right(x, y, z));
        })
    }
};


var abs = unaryOperation(Math.abs);
var log = unaryOperation(Math.log);
var negate = unaryOperation(function (expression) {
    return -expression;
});

var add = binaryOperation(function (left, right) {
    return left + right;
});
var divide = binaryOperation(function (left, right) {
    return left / right;
});
var mod = binaryOperation(function (left, right) {
    return left % right;
});
var multiply = binaryOperation(function (left, right) {
    return left * right;
});
var power = binaryOperation(Math.pow);
var subtract = binaryOperation(function (left, right) {
    return left - right;
});

var square = function (value) {
    return power(value, cnst(2));
};

var parse = function (expression) {
    return expression.trim().split(/[\s]+/).reduce(function (stack, next) {
        var binary = function (func) {
            if (stack.size < 2) {
                throw new SyntaxError("Cannot to parse this Reverse Polish notation expression due RPn expression contain mistake :(");
            }

            var s = stack.pop();
            var f = stack.pop();
            stack.push(new func(f, s));
            return stack;
        };

        var unary = function (func) {
            if (stack.size < 1) {
                throw new SyntaxError("Cannot to parse this Reverse Polish notation expression due RPn expression contain mistake :(");
            }

            stack.push(new func(stack.pop()));
            return stack;
        };

        switch (next) {
            case "+":
                return binary(add);
            case "-":
                return binary(subtract);
            case "*":
                return binary(multiply);
            case "/":
                return binary(divide);
            case "**":
                return binary(power);
            case "%":
                return binary(mod);
            case "abs":
                return unary(abs);
            case "log":
                return unary(log);
            case "negate":
                return unary(negate);
            default:
                if (next.match(/^[0-9\.\-]+$/)) {
                    stack.push(cnst(+next));
                } else {
                    stack.push(variable(next));
                }
                return stack;
        }
    }, [])[0];
};


// var expr = subtract(
//     multiply(
//         cnst(2),
//         variable("x")
//     ),
//     cnst(3)
// );
// console.log(expr(5));

// for (var i = 0; i < 11; i++) {
//     console.log(
//         add(
//             subtract(
//                 square(
//                     variable('x')
//                 ),
//                 multiply(
//                     cnst(2),
//                     variable('x')
//                 )
//             ),
//             cnst(1)
//         )(i)
//     );
// }

// parse("x x 2 - * x * 1 +");