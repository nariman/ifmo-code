;
; Nariman Safiulin (woofilee)
; File: expression.clj
; Created on: May 23, 2016
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

(def sin (operation (fn [x] (Math/sin x))))
(def cos (operation (fn [x] (Math/cos x))))


(defn Constant' [this value]
  (assoc this
    :value value))

(def Constant (constructor Constant'
                {:evaluate (fn [this args] (this :value))
                 :diff     (fn [this name] (Constant 0))
                 :toString (fn [this]      (str (this :value)))}))

(defn Variable' [this name]
  (assoc this
    :name name))

(def Variable (constructor Variable'
                {:evaluate (fn [this args] (args (this :name)))
                 :diff     (fn [this name] (if (= (this :name) name) (Constant 1) (Constant 0)))
                 :toString (fn [this]      (this :name))}))

(defn Operation' [f name diff]
  {:evaluate (fn [this args] (apply f (mapv
                                        (fn [x] (evaluate x args))
                                        (this :operands))))
   :toString (fn [this]      (cons name (mapv
                                          (fn [x] (toString x)) (this :operands))))
   :diff     (fn [this name] (diff (this :operands) name))
   })

(defn Operation [this & args]
    (assoc this
        :args args))

(def operations {'+ Add '- Subtract '* Multiply '/ Divide 'negate Negate 'sin Sin 'cos Cos})

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
;     (Subtract
;         (Multiply
;             (Constant 2)
;             (Variable "x"))
;         (Const 3)))

; (println (test expr))
