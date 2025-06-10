(ns apiav3.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-body]]
            [cheshire.core :as json]
            [apiav3.db :as db]
            [apiav3.transacoes :as transacoes]))

(def usuario (atom ()))

;; (defn get-usuario []
;;   (swap! usuario conj {:idade 30
;;                        :peso 70
;;                        :altura 1.75
;;                        :sexo "Masculino"})
;;  usuario)

(defn como-json [conteudo & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json; charset=utf-8"}
   :body (json/generate-string conteudo)})

(defroutes app-routes
  (GET "/" [] "Hello Word")
	(GET "/saldo"	[]	(como-json	{:saldo	(db/saldo)}))
  (GET "/transacoes" [] )
  (POST "/transacoes"	requisicao
    (if	(transacoes/valida?	(:body	requisicao))
      (->	(db/registrar	(:body	requisicao))
          (como-json	201))
      (como-json	{:mensagem	"Requisição	inválida"}	422)))
(GET "/transacoes"	[]
   (como-json	{:transacoes	(db/transacoes)}))
 (GET "/receitas"	[]
   (como-json	{:transacoes	(db/transacoes-do-tipo "receita")}))
 (GET "/despesas"	[]
   (como-json	{:transacoes	(db/transacoes-do-tipo "despesa")}))
 (route/not-found "Recurso	não	encontrado"))

  ;;  (POST "/cadastro" requisicao (set-usuario (:body requisicao)))
  ;;  (GET "/usuario" [] (get-usuario))
 
(def	app
  (->	(wrap-defaults	app-routes	api-defaults)
      (wrap-json-body {:keywords?	true	:bigdecimals?	true})))