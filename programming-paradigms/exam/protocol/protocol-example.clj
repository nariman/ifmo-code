;
; Nariman Safiulin (woofilee)
; File: protocol-example.clj
; Created on: May 30, 2016
;

(load-file "protocol.clj")

; New loggers
(def console-logger (new-console-logger (log-level :Info)))
(def html-logger (new-html-logger (log-level :Warning)))

; Checking for standart loggers
(.log console-logger "This is Console Logger" (log-level :Info))
(.log html-logger "This is HTML Logger" (log-level :Warning))

; Checking for minimal log level
(.log console-logger "This console message is missed" (log-level :Debug))
(.log html-logger "This HTML message is missed" (log-level :Info))

; Adding loggers to composite logger
(def composite-logger (new-composite-logger (log-level :Info)))
(.addLogger composite-logger console-logger)
(.addLogger composite-logger html-logger)

; Checking for composite logger
(.log composite-logger "This is message, sent by Composite Logger" (log-level :Warning))

; Checking for minimal log composite level
(.log composite-logger "This is message, sent by Composite Logger, is missed" (log-level :Debug))

; But some of the messages are accepted by composite, but filtered by standart logger
(.log composite-logger "This is message, filtered only by HTML logger" (log-level :Info))
