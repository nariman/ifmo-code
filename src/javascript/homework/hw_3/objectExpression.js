/**
 * Nariman Safiulin (woofilee)
 * File: objectExpression.js
 * Created on: Apr 29, 2016
 */

// "use strict";

function ExpressionObject() {
}

// ExpressionObject.prototype.evaluate = function (variables) {
//     throw new Error("Method not implemented")
// };

ExpressionObject.prototype.evaluate = function () {
    return {'x': arguments[0], 'y': arguments[1], 'z': arguments[2]}
};
ExpressionObject.prototype.simplify = function () {
    return this
};

function Const(value) {
    this._expression = value;
}

Const.prototype = Object.create(ExpressionObject.prototype);
Const.prototype.constructor = Const;
Const.prototype.evaluate = function () {
    return this._expression
};
Const.prototype.diff = function () {
    return new Const(0)
};
Const.prototype.toString = function () {
    return this._expression.toString()
};

function Variable(name) {
    this._name = name
}

Variable.prototype = Object.create(ExpressionObject.prototype);
Variable.prototype.constructor = Variable;
Variable.prototype.evaluate = function () {
    return ExpressionObject.prototype.evaluate.apply(this, arguments)[this._name]
};
Variable.prototype.diff = function (variable) {
    if (variable == this._name) {
        return new Const(1)
    }
    return new Const(0)
};
Variable.prototype.toString = function () {
    return this._name
};

function Operation(operator, func, diff, simp) {
    this._operator = operator;
    this._function = func;
    this._derivative = diff;
    this._simplified = simp;
}

Operation.prototype = Object.create(ExpressionObject.prototype);
Operation.prototype.constructor = Operation;

function UnaryOperation(operator, func, diff, simp) {
    function UnaryOperationFunction(expression) {
        Operation.apply(this, [operator, func, diff, simp]);
        this._expression = expression;
    }

    UnaryOperationFunction.prototype = Object.create(Operation.prototype);
    UnaryOperationFunction.prototype.evaluate = function () {
        return this._function(this._expression.evaluate.apply(this._expression, arguments))
    };
    UnaryOperationFunction.prototype.diff = function (variable) {
        return this._derivative(this._expression, variable)
    };
    UnaryOperationFunction.prototype.simplify = function () {
        return this._simplified(this._expression.simplify())
    };
    UnaryOperationFunction.prototype.toString = function () {
        return this._expression.toString() + " " + this._operator
    };

    return UnaryOperationFunction
}

function BinaryOperation(operator, func, diff, simp) {
    function BinaryOperationFunction(left, right) {
        Operation.apply(this, [operator, func, diff, simp]);
        this._left = left;
        this._right = right;
    }

    BinaryOperationFunction.prototype = Object.create(Operation.prototype);
    BinaryOperationFunction.prototype.evaluate = function () {
        return this._function(this._left.evaluate.apply(this._left, arguments), this._right.evaluate.apply(this._right, arguments))
    };
    BinaryOperationFunction.prototype.diff = function (variable) {
        return this._derivative(this._left, this._right, variable)
    };
    BinaryOperationFunction.prototype.simplify = function () {
        return this._simplified(this._left.simplify(), this._right.simplify())
    };
    BinaryOperationFunction.prototype.toString = function () {
        return this._left.toString() + " " + this._right.toString() + " " + this._operator
    };

    return BinaryOperationFunction
}



var Negate = UnaryOperation("negate", function (expression) {
    return -expression
}, function (expression, variable) {
    return new Negate(expression.diff(variable))
}, function (expression) {
    if (expression instanceof Const) {
        return new Const(-expression)
    }

    return new Negate(expression)
});
var Sin = UnaryOperation("sin", Math.sin, function (expression, variable) {
    return new Multiply(new Cos(expression), expression.diff(variable))
}, function (expression) {
    if (expression instanceof Const) {
        return new Const(Math.sin(expression.evaluate()))
    }
    return new Sin(expression)
});
var Cos = UnaryOperation("cos", Math.cos, function (expression, variable) {
    return new Multiply(new Negate(new Sin(expression)), expression.diff(variable))
}, function (expression) {
    if (expression instanceof Const) {
        return new Const(Math.cos(expression.evaluate()))
    }
    return new Cos(expression)
});
// var Abs = UnaryOperation("abs", Math.abs);
// var Log = UnaryOperation("log", Math.log);

var Add = BinaryOperation("+", function (left, right) {
    return left + right
}, function (left, right, variable) {
    return new Add(left.diff(variable), right.diff(variable));
}, function (left, right) {
    if (left instanceof Const && right instanceof Const) {
        return new Const(left.evaluate() + right.evaluate())
    }

    if (left instanceof Const && left.evaluate() == 0) {
        return right
    }

    if (right instanceof Const && right.evaluate() == 0) {
        return left
    }

    return new Add(left, right)
});
var Divide = BinaryOperation("/", function (left, right) {
    return left / right
}, function (left, right, variable) {
    return new Divide(
        new Subtract(
            new Multiply(
                left.diff(variable),
                right
            ),
            new Multiply(
                left,
                right.diff(variable)
            )
        ),
        new Multiply(
            right,
            right
        )
    )
}, function (left, right) {
    if (left instanceof Const) {
        if (right instanceof Const)
            return new Const(left.evaluate() / right.evaluate())
        if (left.evaluate() == 0)
            return new Const(0)
    }

    if (right instanceof Const) {
        return left
    }

    return new Divide(left, right)
});
var Multiply = BinaryOperation("*", function (left, right) {
    return left * right
}, function (left, right, variable) {
    return new Add(
        new Multiply(
            left.diff(variable),
            right
        ),
        new Multiply(
            left,
            right.diff(variable)
        )
    )
}, function (left, right) {
    if (left instanceof Const && right instanceof Const) {
        return new Const(left.evaluate() * right.evaluate())
    }

    if (left instanceof Const) {
        if (left.evaluate() == 0)
            return new Const(0);
        if (left.evaluate() == 1)
            return right;
    }

    if (right instanceof Const) {
        if (right.evaluate() == 0)
            return new Const(0);
        if (right.evaluate() == 1)
            return left;
    }

    return new Multiply(left, right)
});
var Subtract = BinaryOperation("-", function (left, right) {
    return left - right
}, function (left, right, variable) {
    return new Subtract(left.diff(variable), right.diff(variable));
}, function (left, right) {
    if (left instanceof Const && right instanceof Const) {
        return new Const(left.evaluate() - right.evaluate())
    }

    if (left instanceof Const && left.evaluate() == 0) {
        return new Negate(right)
    }

    if (right instanceof Const && right.evaluate() == 0) {
        return left
    }

    return new Subtract(left, right)
});
// var Mod = BinaryOperation("mod", function (left, right) {
//     return left % right
// });
// var Power = BinaryOperation("**", Math.pow);


// var expr = new Subtract(
//     new Multiply(
//         new Const(2),
//         new Variable("x")
//     ),
//     new Const(3)
// );
//
// console.log(expr.evaluate(5));

var parse = function (expression) {
    return expression.split(" ").filter(function (element, index, elements) {
        return element.length > 0;
    }).reduce(function (stack, next) {
        var binary = function (cls) {
            if (stack.size < 1) {
                throw new SyntaxError("Cannot to parse this Reverse Polish notation expression due RPn expression contain mistake :(");
            }

            var s = stack.pop();
            var f = stack.pop();
            stack.push(new cls(f, s));
            return stack;
        };

        var unary = function (cls) {
            if (stack.size < 1) {
                throw new SyntaxError("Cannot to parse this Reverse Polish notation expression due RPn expression contain mistake :(");
            }

            stack.push(new cls(stack.pop()));
            return stack;
        };

        switch (next) {
            case "+":
                return binary(Add);
            case "-":
                return binary(Subtract);
            case "*":
                return binary(Multiply);
            case "/":
                return binary(Divide);
            case "negate":
                return unary(Negate);
            case "sin":
                return unary(Sin);
            case "cos":
                return unary(Cos);
            default:
                if (next.match(/^[0-9\.\-]+$/)) {
                    stack.push(new Const(+next));
                } else {
                    stack.push(new Variable(next));
                }
                return stack;
        }
    }, [])[0];
};

// console.log( parse('x y z * /').diff('x').simplify().toString())