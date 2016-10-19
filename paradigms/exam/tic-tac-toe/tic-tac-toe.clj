;
; Nariman Safiulin (woofilee)
; File: tic-tac-toe.clj
; Created on: May 29, 2016
;

(use '[clojure.string :only (join split)])


(definterface IParty
    ; Helpers
    (getLine [table n])
    (getRow [table n])
    (getPrimaryDiagonal [table])
    (getSecondaryDiagonal [table])

    (isFullTable [table])
    (isPlayerWin [table player])

    ; Main Logic
    (nextMove [table player])
    (whoWins [table first-player second-player])
    (isGameEnded [table first-player second-player])
)

(deftype Party [] IParty
    (getLine [this table n]
        (nth table n)
    )

    (getRow [this table n]
        (mapv #(nth (.getLine this table %) n) (range 3))
    )

    (getPrimaryDiagonal [this table]
        (mapv #(nth (.getLine this table %) %) (range 3))
    )

    (getSecondaryDiagonal [this table]
        (mapv #(nth (.getLine this table %) (- 2 %)) (range 3))
    )

    (isFullTable [this table]
        (reduce #(and %1 %2) (flatten (mapv #(mapv (fn [x] (not= "." x)) (.getLine this table %)) (range 3))))
    )

    (isPlayerWin [this table player]
        (reduce #(or %1 %2)
            (flatten
                (vector
                    (mapv #(apply = (conj (.getLine              this table %) (.sym player))) (range 3))
                    (mapv #(apply = (conj (.getRow               this table %) (.sym player))) (range 3))
                    (       apply = (conj (.getPrimaryDiagonal   this table)   (.sym player))           )
                    (       apply = (conj (.getSecondaryDiagonal this table)   (.sym player))           )
                )
            )
        )
    )

    (nextMove [this table player]
        (assoc-in
            table
            (loop []
                (let [move (mapv dec (.makeMove player table))]
                    (cond
                        (reduce #(or %1 %2) (mapv #(or (< % 0) (> % 2)) move))
                            (do
                                (println "Incorrect cell. Please, select a cell within 1 and 3 for line and row")
                                (recur)
                            )
                        (not= (get-in table move) ".")
                            (do
                                (println "This cell is marked already. Please, select other cell")
                                (recur)
                            )
                        :else move
                    )
                )
            )
            (.sym player)
        )
    )

    (whoWins [this table first-player second-player]
        (cond
            ; (and (.isPlayerWin this table first-player) (.isPlayerWin this table second-player)) true ;  WHAT??? EASY
            (.isPlayerWin this table first-player) first-player
            (.isPlayerWin this table second-player) second-player
            :else false
        )
    )

    (isGameEnded [this table first-player second-player]
        (or
            (.isPlayerWin this table first-player)
            (.isPlayerWin this table second-player)
            (.isFullTable this table)
        )
    )
)

(definterface IPlayer
    (makeMove [table])
)

(deftype Player [name sym] IPlayer
    (makeMove [this table]
        (do
            (println "Enter turn:")
            (mapv #(Integer. %) (filter (fn [x] (< 0 (count x))) (split (read-line) #" ")))
        )
    )
)

(def table [["." "." "."] ["." "." "."] ["." "." "."]])
(def party (Party.))
(def first-player (Player. "Nariman" "N"))
(def second-player (Player. "Anton" "A"))

(loop [current-player first-player wait-player second-player table table party party]
    (println)
    (if (.isGameEnded party table current-player wait-player)
        (do
            (println "Game ended!")
            (let [game-result (.whoWins party table current-player wait-player)]
                (cond
                    (= game-result current-player) (println (str (.name current-player) " wins!"))
                    (= game-result wait-player) (println (str (.name wait-player) " wins!"))
                    :else (println "Nobody wins!")
                )
            )
        )
        (do
            (mapv #(println (join " " %)) table)
            (println)
            (println (str (.name current-player) "'s turn..."))
            (recur wait-player current-player (.nextMove party table current-player) party)
        )
    )
)
