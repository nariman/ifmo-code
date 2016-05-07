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
// ExpressionObject.prototype.postfix
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
Const.prototype.postfix = function () {
    return this._value.toString();
};
Const.prototype.prefix = function () {
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
Variable.prototype.postfix = function () {
    return this._name;
};
Variable.prototype.prefix = function () {
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
    OperationFunction.prototype.postfix = function () {
        return "(" + this._args.map(function (element) {
                return element.parsePostfix();
            }).join(" ") + " " + this._operator + ")";
    };
    OperationFunction.prototype.prefix = function () {
        return "(" + this._operator + " " + this._args.map(function (element) {
                return element.prefix();
            }).join(" ") + ")";
    };

    operations.push({
        'operator': oper,
        'argnum': argnum,
        'class': OperationFunction
    });
    return OperationFunction;
}

var ArcTan = Operation("atan", 1, Math.atan, function (variable, expression) {
    return new Multiply(new Divide(new Const(1), new Add(new Const(1), new Multiply(expression, expression))), expression.diff(variable));
}, function (expression) {
    return new ArcTan(expression);
});
var Cos = Operation("cos", 1, Math.cos, function (variable, expression) {
    return new Multiply(new Negate(new Sin(expression)), expression.diff(variable));
}, function (expression) {
    return new Cos(expression);
});
var Exp = Operation("exp", 1, Math.exp, function (variable, expression) {
    return new Multiply(new Exp(expression), expression.diff(variable));
}, function (expression) {
    return new Exp(expression);
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


// Valid for prefix notation only
// But takes append function, for saving order of operands
var buildExpression = function (expression, append) {
    var result = expression.reduce(function (stack, element, index, elements) {
        if (element instanceof Array) {
            stack.push(buildExpression(element, append));
        } else if (!operations.some(function (operation) {
                if (element == operation.operator) {
                    if (stack.length < operation.argnum) {
                        throw new SyntaxError("[BUILDER] Not enough arguments in expression for operation \"" + 
                            operation.operator + "\" - operator #" + (index + 1) + ". Needed: " + operation.argnum + 
                            ". Found: " + stack.length + ".");
                    }

                    var parts = [];
                    for (var i = operation.argnum; i --> 0;) {
                        append.call(parts, stack.pop());
                    }

                    stack.push(operation.class.apply(new (operation.class)(), parts));
                    return true;
                }

                return false;
            })) {
            if (element.match(/^[0-9\.\-]+$/)) {
                stack.push(new Const(+element));
            // } else if (element.match(/^[a-z]$/)) {
            } else if (element.match(/^[xyz]$/)) {
                stack.push(new Variable(element));
            } else {
                throw new SyntaxError("[BUILDER] Unexpected symbols in token - token #" + (index + 1));
            }
        }

        return stack;
    }, []);

    if (result.length != 1) {
        throw new SyntaxError("[BUILDER] Number of arguments in expression after building not equals 1");
    }

    return result[0];
};

var parseExpression = function (expression, token, depthIn, depthOut, append) {
    expression = expression.trim();
    var stack = {'level': 0, 0: []};
    var tokenCounter = 0;

    while (expression.length > 0) {
        var matched = expression.match(token); // Always return something in our case!
        var element = matched[1];
        tokenCounter++;

        if (element[0] == depthIn) {
            stack.level++;
            stack[stack.level] = [];
        } else if (element == depthOut) {
            if (stack.level === 0) {
                throw new SyntaxError("[PARSER] Unexpected bracket - token #" + tokenCounter);
            }

            stack[stack.level - 1].push(stack[stack.level]);
            delete stack[stack.level];
            stack.level--;
        } else {
            stack[stack.level].push(element);
        }

        expression = expression.replace(token, "");
    }

    if (stack.level !== 0) {
        throw new SyntaxError("[PARSER] Expression contains incorrect bracket sequence");
    }

    return buildExpression(stack[0], append);
};

var parsePostfix = function(expression) {
    // Int/Float -OR- "(" -OR- ")" -OR- Variable FROM START
    return parseExpression(expression, /^[\s]*((\-?\d+(\.\d+)?)|(\()|(\))|([^\d\(\)\s]+))/, "(", ")", Array.prototype.unshift);
};

var parsePrefix = function(expression) {
    // Int/Float -OR- "(" -OR- ")" -OR- Variable FROM END
    return parseExpression(expression, /((\-?\d+(\.\d+)?)|(\()|(\))|([^\d\(\)\s]+))[\s]*$/, ")", "(", Array.prototype.push);
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
