;
; Nariman Safiulin (woofilee)
; File: brainfuck.clj
; Created on: May 29, 2016
;

(definterface IMachine
    (add [])
    (subtract [])
)

(deftype Machine
    [
        ^{:unsynchronized-mutable true} memory
        ^{:unsynchronized-mutable true} memory-position
    ]

    IMachine
    (add []
        (set! (assoc memory memory-position ()))
    )
)

(defn interpretate [program-code]
    (loop [memory [0] program-position 0 memory-position 0]
        (case (get program-code program-position)
            \+
                (recur
                    (assoc
                        memory
                        memory-position
                        (inc (nth memory memory-position))
                    )
                    (inc program-position)
                    memory-position
                )
            \-
                (recur
                    (assoc
                        memory
                        memory-position
                        (dec (nth memory memory-position))
                    )
                    (inc program-position)
                    memory-position
                )
            \.
                (do
                    (print (char (nth memory memory-position)))
                    (recur memory (inc program-position) memory-position)
                )
            \,
                (do
                    (assoc (int (read-line)))
                    (recur memory (inc program-position) memory-position)
                )
            (println "Program Complete!")
        )
    )
)


(def hello-world "++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.")

(interpretate hello-world)
