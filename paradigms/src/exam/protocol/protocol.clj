;
; Nariman Safiulin (woofilee)
; File: protocol.clj
; Created on: May 30, 2016
;

(definterface ILogger
    (log [message message-level])
    (setLevel [level])
)

(definterface ICompositeLogger
    (addLogger [logger])
    (removeLogger [logger])
)

(def log-level {
    :Debug 1
    :Info 2
    :Warning 3
    :Error 4
})

(deftype ConsoleLogger [^{:unsynchronized-mutable true} log-level] ILogger
    (log [this message message-level]
        (if (>= message-level log-level)
            (println (str "[ConsoleLogger] " "[" log-level "] " message))
        )
    )

    (setLevel [this level]
        (set! log-level level)
    )
)

(deftype HTMLLogger [^{:unsynchronized-mutable true} log-level] ILogger
    (log [this message message-level]
        (if (>= message-level log-level)
            (println (str "<div class\"log\">" "[" log-level "] " message "</div>"))
        )
    )

    (setLevel [this level]
        (set! log-level level)
    )
)

(deftype CompositeLogger
    [
        ^{:unsynchronized-mutable true} loggers
        ^{:unsynchronized-mutable true} log-level
    ]

    ; Composite Logger Methods
    ILogger
    
    (log [this message message-level]
        (do
            (mapv #(if (>= message-level log-level) (.log % message message-level)) loggers)
            nil
        )
    )

    (setLevel [this level]
        (set! log-level level)
    )

    ; Composite Logger Methods
    ICompositeLogger

    (addLogger [this logger]
        (if (instance? ILogger logger)
            (do
                (set! loggers (conj loggers logger))
                true
            )
            false
        )
    )

    (removeLogger [this logger]
        (set! loggers (disj loggers logger))
    )
)

(defn new-console-logger [level] (ConsoleLogger. level))
(defn new-html-logger [level] (HTMLLogger. level))
(defn new-composite-logger [level] (CompositeLogger. #{} level))
