(ns conversor.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clj-http.client :as http-client]
            [cheshire.core :refer [parse-string]])
  (:gen-class))

;; Chave da API definida diretamente no código
(def chave "fca_live_9DK8iy1UGUdAo4n8xczXbMQWa3E4CqScCkW2BHBQ")

;; URL do serviço de câmbio 
(def api-url "https://api.freecurrencyapi.com/v1/latest")

;; Opções da linha de comando
(def opcoes-do-programa
  [["-d" "--de moeda base" "moeda base para conversão" :default "USD"]
   ["-p" "--para moeda destino" "moeda a qual queremos saber o valor"]])

;; Função que consulta a API e retorna a cotação
(defn obter-cotacao
  [de para]
  (let [resposta (http-client/get api-url
                                  {:query-params {"apikey" chave
                                                  "base_currency" de
                                                  "currencies" para}})
        corpo (:body resposta)
        dados (parse-string corpo true)]
    (get-in dados [:data (keyword para)])))

;; Função principal
(defn -main [& args]
  (let [{:keys [options]} (parse-opts args opcoes-do-programa)
        de (:de options)
        para (:para options)
        valor (obter-cotacao de para)]
    (println (str "1 " de " = " valor " " para))))

;;lein run --de USD --para BRL pra testar