;
; Nariman Safiulin (woofilee)
; File: expression.clj
; Created on: May 15, 2016
;

(defn expression-object [func]
    (fn [variables]
        (func variables)))

(defn constant [value]
    (expression-object
        (fn [variables] value)))

(defn variable [name]
    (expression-object
        (fn [variables]
            (variables name))))

(defn operation [func]
    (fn [& args]
        (expression-object
            (fn [variables]
                (apply func 
                    (map (fn [arg] (arg variables)) args))))))

(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def divide
    (operation
        (fn
            ([a b] (/ (double a) b))
            ([a b & more] (reduce divide (divide a b) more)))))
(def negate (operation -))

(def operations {'+ add '- subtract '* multiply '/ divide 'negate negate})

(defn build-expression [expression]
    (cond
        (number? expression) (constant expression)
        (symbol? expression) (variable (str expression))
        (list? expression) (apply
            (operations (first expression))
            (map build-expression (rest expression)))))

(defn parseFunction [expression]
    (build-expression (read-string expression)))

; (def expr 
;     (subtract
;         (multiply 
;             (constant 2) 
;             (variable "x")) 
;         (constant 3)))

; (println (test expr))
