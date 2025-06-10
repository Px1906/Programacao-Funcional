(ns calcapp.core
  (:gen-class)
  (:require [clj-http.client :as http-client]
            [cheshire.core :as json]))


(defn novoCadastro []
  (let [cadastro {}] 
    (println "NOVO CADASTRO:")
    (println "Digite o número com base no seu sexo: \n1. Masculino \n2. Feminino")
    
    (let [sexo (read)
          cadastro (assoc cadastro :sexo sexo)] 
          
      (println "Digite sua idade:")
      (let [idade (read)
            cadastro (assoc cadastro :idade idade)] 
            
        (println "Digite o seu peso:")
        (let [peso (read)
              cadastro (assoc cadastro :peso peso)] 

          ;; EXIBINDO os dados que serão enviados
          (println "\nDados cadastrados:" cadastro)
          (println "Enviando dados para o servidor...")

          ;; ADICIONADO: envia o map convertido para JSON via POST
          (let [resposta (http-client/post "http://localhost:3000/usuario"
                                           {:headers {"Content-Type" "application/json"}
                                            :body (json/generate-string cadastro)})]
            (println "Resposta do servidor:" (:status resposta)))

          cadastro))))) 

;; MANTIDO: menu de cadastro (com consulta GET)
(defn menuCadastro []
  (println "OPÇÕES DE CADASTRO:\n1. Novo cadastro\n2. Consulta")
  (case (read)
    1 (novoCadastro)
    2 (println (:body (http-client/get "http://localhost:3000/usuario")))))

;; MANTIDO: menu principal
(defn menu []
  (println "MENU:\n1. Cadastro\n2. Ganho calórico\n3. Perda calórica")
  (case (read)
    1 (menuCadastro)
    ;; 2 (ganhoCalorico)
    ;; 3 (perdaCalorica)
    ))

(defn -main
  [& args]
  (menu))