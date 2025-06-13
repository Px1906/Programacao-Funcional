(ns calcapp.core
  (:gen-class)
  (:require [clj-http.client :as http-client]
            [cheshire.core :as json]
            [calcapp.tratamentos :as tratamentos]
            ;;[java-time :as jt]
            ))

(defn novoCadastro []
  (println "\nNOVO CADASTRO:")
  (println "Digite o numero com base no seu sexo: \n1. Masculino \n2. Feminino")
  (let [sexo (tratamentos/ler1ou2)]
    (println "Digite sua idade:")
    (let [idade (tratamentos/lerIdade)]
      (println "Digite seu peso:")
      (let [peso (tratamentos/lerInt)
            cadastro {:sexo sexo :idade idade :peso peso}]
        (println "Dados cadastrados com sucesso")
        (http-client/post "http://localhost:3001/cadastro"
                          {:headers {"Content-Type" "application/json"}
                           :body (json/generate-string cadastro)})
        cadastro))))

(defn menuCadastro []
  (println "\nOPCOES DE CADASTRO:\n1. Novo cadastro\n2. Consulta")
  (case (read)
    1 (novoCadastro)
    2 (let [resposta (http-client/get "http://localhost:3001/cadastro"
                                      {:headers {"Accept" "application/json"}})
            corpo-json (:body resposta)
            mapa (json/parse-string corpo-json true)
            {:keys [sexo idade peso]} mapa]
        (println "\nSexo:" (if (= sexo 1) "Masculino" "Feminino"))
        (println "Idade:" idade "anos")
        (println "Peso:" peso "Kg"))
    (println "Opcao invalida")))

(defn lancarGanhoCalorico []
  (println "\nREGISTRO DE GANHO CALORICO (ALIMENTO):")
  (println "Digite o nome do alimento:")
  (let [alimento (tratamentos/lerStringSemNumeros)]
    (println "Digite a quantidade (em gramas):")
    (let [quantidade (tratamentos/lerInt)]
      (println "Digite a data (formato: dd-mm-yyyy):")
      (let [data (tratamentos/lerData)
            registro {:alimento alimento 
                      :quantidade quantidade 
                      :data (str data)}]
        (println "\nEnviando dados do alimento para o servidor...")
        (try
          (let [resposta (http-client/post "http://localhost:3001/ganho"
                                           {:headers {"Content-Type" "application/json"}
                                            :body (json/generate-string registro)})]
            (println "Resposta do servidor:" (:status resposta))
            (println "Corpo da resposta:" (:body resposta)))
          (catch Exception e
            (println "Erro ao enviar dados para o servidor:" (.getMessage e))))
        registro))))

(defn lancarPerdaCalorica []
  (println "\nREGISTRO DE PERDA CALORICA (ATIVIDADE FISICA):")
  (println "Digite o nome da atividade fisica:")
  (let [atividade (tratamentos/lerStringSemNumeros)]
    (println "Digite a duracao (em minutos):")
    (let [duracao (tratamentos/lerInt)]
      (println "Digite a data (formato: dd-mm-yyyy):")
      (let [data (tratamentos/lerData)
            registro {:atividade atividade
                      :duracao duracao
                      :data (str data)}]
        (println "nEnviando dados da atividade para o servidor ... ")
        (try
          (let [resposta (http-client/post "http://localhost:3001/perda"
                                           {:headers {"Content-Type" "application/json"}
                                            :body (json/generate-string registro)})]
            (println "Resposta do servidor:" (:status resposta))
            (println "Corpo da resposta:" (:body resposta)))
          (catch Exception e
            (println "Erro ao enviar dados para o servidor:" (.getMessage e))))
        registro))))

(defn menu []
  (println "\nMENU:\n1. Cadastro\n2. Ganho calorico\n3. Perda calorica")
  (let [opcao (tratamentos/lerOpcoesMenu)]
    (case opcao
      1 (do (menuCadastro) (recur))
      2 (do (lancarGanhoCalorico) (recur))
      3 (do (lancarPerdaCalorica) (recur))
      (do (println "Opcao invalida") (recur)))))

(defn -main
  "Ponto de entrada do programa."
  [& args]
  (menu))