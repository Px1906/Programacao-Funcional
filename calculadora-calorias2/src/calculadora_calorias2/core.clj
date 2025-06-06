(ns calculadora-calorias2.core
  (:gen-class))

(defn novoCadastro []
    (let [cadastro []]
        (println "NOVO CADASTRO:")
        (println "Gênero: 1. Masculino 2. Feminino")
        (let [cadastro (conj cadastro (int (read)))]
            (printf "Idade: ")
            (let [cadastro (conj cadastro (int (read)))]
                (printf "\nPeso: ")
                (let [cadastro (conj cadastro (int (read)))]
                    (println "")
                    (reverse cadastro)
                )
            )
        )
    )
)

;; (defn perdaCalorica []
;;     (let )
;;     (println "Digite o nome do exercício que você praticou")
;; )

;; (defn ganhoCalorico []
    
;; )

(defn visualizarCadastro []
    (let [[genero idade peso altura] @cadastro]
        (printf "Genero: %s\nIdade: %d anos\nPeso: %dKg\nAltura: %.2fm\n\n" (if (= genero 1) "Masculino" "Feminino") idade peso altura)
    )
)

(defn menuCadastro []
    (println "OPCOES DE CADASTRO:\n1. Novo cadastro\n2. Visualizar cadastro\n")
    (case (read)
        1 (novoCadastro)    
        2 (visualizarCadastro)
    )
)

(defn menu []
    (println "MENU:\n1. Cadastro\n2. Ganho calorico\n3. Perda calorica\n")
    (case (read)
        1 (menuCadastro)
        ;; 2 (perdaCalorica)
        ;; 3 (ganhoCalorico)
    )
    (recur)
)


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (menu)
