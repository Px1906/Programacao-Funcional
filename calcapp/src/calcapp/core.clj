(ns calcapp.core
  (:gen-class)
  (:require [clj-http.client :as http-client]
            [cheshire.core :as json]))

(defn novoCadastro []
  (println "NOVO CADASTRO:")
  (println "Digite o número com base no seu sexo: \n1. Masculino \n2. Feminino")
  (let [sexo (read)]
    (println "Digite sua idade:")
    (let [idade (read)]
      (println "Digite seu peso:")
      (let [peso (read)
            cadastro {:sexo sexo :idade idade :peso peso}]
        (println "\nDados cadastrados:" cadastro)
        (println "Enviando dados para o servidor...")
        (let [resposta (http-client/post "http://localhost:3001/cadastro"
                                         {:headers {"Content-Type" "application/json"}
                                          :body (json/generate-string cadastro)})]
          (println "Resposta do servidor:" (:status resposta))
          (println "Corpo da resposta:" (:body resposta)))
        cadastro))))

(defn menuCadastro []
  (println "OPÇÕES DE CADASTRO:\n1. Novo cadastro\n2. Consulta")
  (case (read)
    1 (novoCadastro)
    2 (let [resposta (http-client/get "http://localhost:3001/cadastro"
                                      {:headers {"Accept" "application/json"}})
            corpo-json (:body resposta)
            mapa (json/parse-string corpo-json true)
            {:keys [sexo idade peso]} mapa]
        (println "\n--- Dados recebidos do servidor ---")
        (println "Map Clojure:" mapa)
        (println "Sexo:" (if (= sexo 1) "Masculino" "Feminino"))
        (println "Idade:" idade)
        (println "Peso:" peso))
    (println "Opção inválida")))

(defn menuGanhoCalorico []
  (println "\nREGISTRO DE GANHO CALÓRICO (ALIMENTO):")
  (println "Digite o nome do alimento:")
  (let [alimento (read)]
    (println "Digite a quantidade (em gramas):")
    (let [quantidade (read)]
      (println "Digite a data (formato: dd-mm-yyyy):")
      (let [data (read)
            registro {:alimento alimento :quantidade quantidade :data data}]
        (println "\nEnviando dados do alimento para o servidor...")
        (let [resposta (http-client/post "http://localhost:3001/ganho";;Fiquei na duvida se coloca cadastro tbm
                                         {:headers {"Content-Type" "application/json"}
                                          :body (json/generate-string registro)})]
          (println "Resposta do servidor:" (:status resposta))
          (println "Corpo da resposta:" (:body resposta)))
        registro))))


        (defn menuPerdaCalorica []
  (println "\nREGISTRO DE PERDA CALÓRICA (ATIVIDADE FÍSICA):")
  (println "Digite o nome da atividade física:")
  (let [atividade (read)]
    (println "Digite a duração (em minutos):")
    (let [duracao (read)]
      (println "Digite a data (formato: dd-mm-yyyy):")
      (let [data (read)
            registro {:atividade atividade :duracao duracao :data data}]
        (println "\nEnviando dados da atividade para o servidor...")
        (let [resposta (http-client/post "http://localhost:3001/perda";;Fiquei na duvida se coloca cadastro tbm
                                         {:headers {"Content-Type" "application/json"}
                                          :body (json/generate-string registro)})]
          (println "Resposta do servidor:" (:status resposta))
          (println "Corpo da resposta:" (:body resposta)))
        registro))))


(defn menu []
  (println "MENU:\n1. Cadastro\n2. Ganho calórico\n3. Perda calórica")
  (case (read)
    1 (menuCadastro)
    2 (menuGanhoCalorico)
    3 (menuPerdaCalorica)
    (println "Opção inválida"))
   (recur) ;; se quiser loop contínuo
  )

(defn -main
  "Ponto de entrada do programa."
  [& args]
  (menu))
