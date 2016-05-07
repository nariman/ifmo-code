/**
 * Nariman Safiulin (woofilee)
 * File: objectExpression.js
 * Created on: Apr 29, 2016
 */

// "use strict";

var operations = [];
var variables = {};

function ExpressionObject() {
}

ExpressionObject.prototype.evaluate = function () {
    return {'x': arguments[0], 'y': arguments[1], 'z': arguments[2]};
};
// ExpressionObject.prototype.evaluate = function (variables) {
//     // throw new Error("Method not implemented");
//     return null;
// };
// ExpressionObject.prototype.diff
ExpressionObject.prototype.simplify = function () {
    return this;
};
// ExpressionObject.prototype.prefix
// ExpressionObject.prototype.toString

function Const(value) {
    this._value = value;
}

Const.prototype = Object.create(ExpressionObject.prototype);
Const.prototype.constructor = Const;
Const.prototype.evaluate = function (variables) {
    return this._value;
};
Const.prototype.diff = function (variable) {
    return new Const(0);
};
Const.prototype.prefix = function () {
    return this._value.toString();
};
Const.prototype.toString = function () {
    return this._value.toString();
};

function Variable(name) {
    if (variables[name] !== undefined) {
        return variables[name];
    }

    this._name = name;
    variables[name] = this;
}

Variable.prototype = Object.create(ExpressionObject.prototype);
Variable.prototype.constructor = Variable;
// Variable.prototype.evaluate = function (variables) {
//     return variables[this._name];
// };
Variable.prototype.evaluate = function () {
    return ExpressionObject.prototype.evaluate.apply(this, arguments)[this._name];
};
Variable.prototype.diff = function (variable) {
    if (variable == this._name) {
        return new Const(1);
    }
    return new Const(0);
};
Variable.prototype.prefix = function () {
    return this._name;
};
Variable.prototype.toString = function () {
    return this._name;
};

function Operation(oper, argnum, func, diff, simp) {
    function OperationFunction() {
        this._operator = oper;
        this._argnum = argnum;
        this._function = func;
        this._derivative = diff;
        this._simple = simp;
        this._args = [].slice.apply(arguments); // from function scope!
        return this;
    }

    OperationFunction.prototype = Object.create(ExpressionObject.prototype);
    OperationFunction.prototype.constructor = OperationFunction;
    OperationFunction.prototype.args = function () {
        return this._args;
    };
    // OperationFunction.prototype.evaluate = function (variables) {
    //     return this._function.apply(this, this._args.map(function (arg) {
    //         return arg.evaluate(variables);
    //     }));
    // };
    OperationFunction.prototype.evaluate = function () {
        var args = arguments;
        return this._function.apply(this, this._args.map(function (arg) {
            return arg.evaluate.apply(arg, args);
        }));
    };
    OperationFunction.prototype.diff = function (variable) {
        return this._derivative.apply(this, [variable].concat(this._args));
    };
    OperationFunction.prototype.simplify = function () {
        var simplifiedArgs = this._args.map(function (arg) {
            return arg.simplify();
        });

        if (simplifiedArgs.every(function (arg) {
                return arg instanceof Const;
            })) {
            return new Const(this._function.apply(this, simplifiedArgs.map(function (arg) {
                return arg.evaluate();
            })));
        }

        return this._simple.apply(this, simplifiedArgs);
    };
    OperationFunction.prototype.prefix = function () {
        return "(" + this._operator + " " + this._args.map(function (element) {
                return element.prefix();
            }).join(" ") + ")";
    };
    OperationFunction.prototype.toString = function () {
        return this._args.join(" ") + " " + this._operator;
    };

    operations.push({
        'operator': oper,
        'argnum': argnum,
        'class': OperationFunction
    });
    return OperationFunction;
}

var Cos = Operation("cos", 1, Math.cos, function (variable, expression) {
    return new Multiply(new Negate(new Sin(expression)), expression.diff(variable));
}, function (expression) {
    return new Cos(expression);
});
var Negate = Operation("negate", 1, function (expression) {
    return -expression;
}, function (variable, expression) {
    return new Negate(expression.diff(variable));
}, function (expression) {
    if (expression instanceof Negate) {
        return expression.args()[0];
    }
    return new Negate(expression);
});
var Sin = Operation("sin", 1, Math.sin, function (variable, expression) {
    return new Multiply(new Cos(expression), expression.diff(variable));
}, function (expression) {
    return new Sin(expression);
});

var Add = Operation("+", 2, function (left, right) {
    return left + right;
}, function (variable, left, right) {
    return new Add(left.diff(variable), right.diff(variable));
}, function (left, right) {
    if (left instanceof Const && left.evaluate() === 0) {
        return right;
    }
    if (right instanceof Const && right.evaluate() === 0) {
        return left;
    }
    return new Add(left, right);
});
var Divide = Operation("/", 2, function (left, right) {
    return left / right;
}, function (variable, left, right) {
    return new Divide(
        new Subtract(
            new Multiply(left.diff(variable), right),
            new Multiply(left, right.diff(variable))
        ),
        new Multiply(right, right)
    );
}, function (left, right) {
    if (left instanceof Const && left.evaluate() === 0) {
        return new Const(0);
    }
    if (right instanceof Const) {
        if (right.evaluate() == 1)
            return left;
        if (right.evaluate() == -1)
            return new Negate(left);
    }
    return new Divide(left, right);
});
var Multiply = Operation("*", 2, function (left, right) {
    return left * right;
}, function (variable, left, right) {
    return new Add(
        new Multiply(left.diff(variable), right),
        new Multiply(left, right.diff(variable))
    );
}, function (left, right) {
    if (left instanceof Const) {
        if (left.evaluate() === 0)
            return new Const(0);
        if (left.evaluate() == 1)
            return right;
    }
    if (right instanceof Const) {
        if (right.evaluate() === 0)
            return new Const(0);
        if (right.evaluate() == 1)
            return left;
    }
    return new Multiply(left, right);
});
var Subtract = Operation("-", 2, function (left, right) {
    return left - right;
}, function (variable, left, right) {
    return new Subtract(left.diff(variable), right.diff(variable));
}, function (left, right) {
    if (left instanceof Const && left.evaluate() === 0) {
        return new Negate(right);
    }
    if (right instanceof Const && right.evaluate() === 0) {
        return left;
    }
    return new Subtract(left, right);
});

var parsePrefix = function (expression) {
    println(expression);
    var result = expression.split(" ").reduce(function (stack, element, index, elements) {
        if (element.length === 0) {
            return stack;
        }

        while (element.length > 0) {
            if (element[0] == '(') {
                stack.push('(');
                element = element.substring(1);
            } else if (element[0] == ')') {
                stack.push(')');
                element = element.substring(1);
            } else {
                stack.push(element);
                break;
            }
        }

        return stack;
    }, []).reduceRight(function (stack, element, index, elements) {
        if (element == ')') {
            stack.level++;
            stack[stack.level] = [];
        } else if (element[0] == '(') {
            if (stack.level === 0) {
                throw new SyntaxError("[PARSER] Error due parsing: unexpected opening bracket - token #" + (index + 1));
            } else if (stack[stack.level].length - 1 !== 0) {
                throw new SyntaxError("[PARSER] Error due parsing: number of arguments in brackets after parsing not equals 1 - token #" + (index + 1));
            }

            stack[stack.level - 1].push(stack[stack.level].pop());
            delete stack[stack.level];
            stack.level--;
        } else if (!operations.some(function (operation) {
                if (element == operation.operator) {
                    if (stack[stack.level].length < operation.argnum) {
                        throw new SyntaxError("[PARSER] Error due parsing: not enough arguments in expression for operation \"" + operation.operator + "\" - token #" + (index + 1));
                    }

                    var parts = [];
                    for (var i = operation.argnum; i-- > 0;) {
                        parts.push(stack[stack.level].pop());
                    }

                    stack[stack.level].push(operation.class.apply(new (operation.class)(), parts));
                    return true;
                }

                return false;
            })) {
            if (element.match(/^[0-9\.\-]+$/)) {
                stack[stack.level].push(new Const(+element));
            } else if (element.match(/^[xyz]$/)) {
                stack[stack.level].push(new Variable(element));
            } else {
                throw new SyntaxError("[PARSER] Error due parsing: unexpected symbols in token - token #" + (index + 1));
            }
        }

        return stack;
    }, {'level': 0, 0: []});

    if (result.level !== 0) {
        throw new SyntaxError("[PARSER] Error due parsing: expression contains incorrect bracket sequence");
    } else if (result[0].length - 1 !== 0) {
        throw new SyntaxError("[PARSER] Error due parsing: number of arguments in expression after parsing not equals 1");
    }

    return result[0].pop();
};

// var expr = new Subtract(
//     new Multiply(
//         new Const(2),
//         new Variable("x")
//     ),
//     new Const(3)
// );
// console.log(expr.evaluate(5));

// console.log( parse('x y z * /').diff('x').simplify().toString())
