(ns nota5.handler
   (:require [compojure.core :refer :all]
             [compojure.route :as route]
             [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
             [ring.middleware.json :refer [wrap-json-body]]
             [cheshire.core :as json]
             [nota5.banco-de-dados :as bd]))

(def usuario (atom ()))

(def bdGastoCalorico (atom ()))

(def bdGanhoCalorico (atom ()))

(defn como-json [data]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string data)})

(defn setUsuario [dados]
   (let [novo-usuario {:sexo (:sexo dados)
                      :idade (:idade dados)
                      :peso (:peso dados)}]
    (reset! usuario novo-usuario) novo-usuario))

(defroutes app-routes
  (GET "/" [] "Hello Word")

  (GET "/cadastro" [] (como-json @usuario))
  (GET "/bancoDeDados" [] (como-json {:ganho @bdGanhoCalorico :gasto @bdGastoCalorico}))
  
  ;; (GET "/totalCalorico" [] (como-json ...))
  ;; (GET "/consumoCalorico" [] (como-json ...))
  ;; (GET "/perdaCalorica" [] (como-json ...))

  ;----------------------------------------------------------------------------------------------------------------------------------------
    
  (POST "/perda" {body :body} (como-json (bd/registro body bdGastoCalorico)))

  (POST "/ganho" {body :body} (como-json (bd/registro body bdGanhoCalorico)))

  (POST "/cadastro" {body :body}
    (do
      (setUsuario body)
      (como-json @usuario)))

  (route/not-found "Recurso não encontrado"))

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true :bigdecimals? true})
      (wrap-defaults api-defaults)))