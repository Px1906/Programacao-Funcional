(ns nota5.defspersonalizados
  (:require [nota5.handler :refer :all]         ;; Importa o manipulador principal da aplicação.
            [clj-http.client :as http]           ;; Biblioteca para fazer requisições HTTP.
            [ring.adapter.jetty :refer [run-jetty]] ;; Adaptador para rodar o servidor no Jetty.
            [cheshire.core :as json]))           ;; Biblioteca para manipulação de JSON.

;; Define uma porta padrão para o servidor da aplicação.
(def porta-padrao 3001)

;;Constrói o endereço de uma rota específica no servidor local.
(defn endereco-para [rota]
  (str "http://localhost:" porta-padrao rota))

;; Composição de funções para fazer requisições GET baseadas na rota fornecida.
(def requisicao-para (comp http/get endereco-para))

;; Função que obtém o conteúdo da resposta de uma requisição GET.
(defn conteudo [rota]
  (:body (requisicao-para rota)))

;; Variável mutável (atom) para armazenar a instância do servidor.
(def servidor (atom nil))

;; Função para iniciar o servidor na porta especificada.
(defn iniciar-servidor [porta]
  (swap! servidor
         (fn [_] (run-jetty app {:port porta :join? false}))))

;; Função para parar o servidor em execução.
(defn parar-servidor []
  (.stop @servidor))

;; Função que formata um objeto como JSON para envio em requisições.
(defn conteudo-como-json [transacao]
  {:content-type :json
   :body (json/generate-string transacao) ;; Converte o mapa para uma string JSON.
   :throw-exceptions false}) ;; Desabilita exceções automáticas ao lidar com respostas.

;; Função que cria uma estrutura JSON para uma despesa.
(defn despesa [valor]
  (conteudo-como-json {:valor valor :tipo "despesa"}))

;; Função que cria uma estrutura JSON para uma receita.
(defn receita [valor]
  (conteudo-como-json {:valor valor :tipo "receita"}))
