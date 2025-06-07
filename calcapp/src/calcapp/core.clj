(ns calcapp.core
  (:gen-class))

(require '[clj-http.client :as http-client])

(defn novoCadastro []
  (let [cadastro []]
    (println "NOVO CADASTRO:")
    (println "Digige o numero com base no seu sexo: \n1. Masculino \n2. Feminino")
    (let [cadastro (conj cadastro (int (read)))]
      (println "Digite sua idade: ")
      (let [cadastro (conj cadastro (int (read)))]
        (println "Digite o seu peso: ")
        (let [cadastro (conj cadastro (int (read)))]
          (println "\n")
          cadastro)))))

;; (defn perdaCalorica []
;;     (let )
;;     (println "Digite o nome do exercício que você praticou")
;; )

;; (defn ganhoCalorico []

;; )

(defn menuCadastro []
  (println "OPCOES DE CADASTRO:\n1. Novo cadastro\n2. Consulta")
  (case (read)
    1 (novoCadastro)
    2 (println (:body (http-client/get "http://localhost:3000/usuario")))))

(defn menu []
  (println "MENU:\n1. Cadastro\n2. Ganho calorico\n3. Perda calorica\n")
  (case (read)
    1 (menuCadastro)
    ;; 2 (perdaCalorica)
    ;; 3 (ganhoCalorico)
    )
  ;;(recur)
  )


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (menu))