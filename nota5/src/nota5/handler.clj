(ns nota5.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-body]]
            [cheshire.core :as json]
            [nota5.db :as db]
            [nota5.lancamentos :as lancamentos]))

(defn como-json [conteudo & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json; charset=utf-8"}
   :body (json/generate-string conteudo)})

(defroutes app-routes
;;TROCAR
  (GET "/" [] "Hello Word")
  (GET "/saldo" [] (como-json {:saldo (db/saldo)}))
  (POST "/transacoes" requisicao
    (if (lancamentos/valida? (:body requisicao))
      (-> (db/registrar (:body requisicao))
          (como-json 201))
      (como-json {:mensagem "Requisição inválida"} 422)))
  (GET "/transacoes" []
    (como-json {:transacoes (db/transacoes)}))
  (GET "/receitas" []
    (como-json {:transacoes (db/transacoes-do-tipo "receita")}))
  (GET "/despesas" []
    (como-json {:transacoes (db/transacoes-do-tipo "despesa")}))
  (route/not-found "Recurso não encontrado"))
;;  (POST "/cadastro" requisicao (set-usuario (:body requisicao)))
;;  (GET "/usuario" [] (get-usuario))

(def app
  (-> (wrap-defaults app-routes api-defaults)
      (wrap-json-body {:keywords? true :bigdecimals? true})))