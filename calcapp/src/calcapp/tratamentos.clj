(ns calcapp.tratamentos)

(defn lerInt []
  (loop []
    (let [input (read-line)]
      (if (re-matches #"-?\d+" input)
        (Integer/parseInt input)
        (do
          (println "Por favor digite um numero interio")
          (recur))))))

(defn lerIdade []
  (loop []
    (let [input (read-line)]
      (if (and (re-matches #"\d+" input)
               (let [idade (Integer/parseInt input)]
                 (and (>= idade 8) (<= idade 125))))
        (Integer/parseInt input)
        (do
          (println "Por favor, digite uma idade entre 8 e 125 anos.")
          (recur))))))

(defn ler1ou2 []
  (loop []
    (let [input (read-line)]
      (if (or (= input "1") (= input "2"))
        (Integer/parseInt input)
        (do
          (println "Por favor, digite 1 ou 2")
          (recur))))))

(defn lerOpcoesMenu []
  (loop []
    (let [input (read-line)]
      (if (or (= input "1") (= input "2") (= input "3"))
        (Integer/parseInt input)
        (do
          (println "Por favor, digite 1, 2 ou 3")
          (recur))))))