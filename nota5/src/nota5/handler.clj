(ns nota5.handler
   (:require [compojure.core :refer :all]
             [compojure.route :as route]
             [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
             [ring.middleware.json :refer [wrap-json-body]]
             [cheshire.core :as json]
             [nota5.db :as db]
             [nota5.lancamentos :as lancamentos]
             [clj-http.client :as client]))
  

(defn como-json [conteudo & [status]]
  {:status (or status 200)
  :headers {"Content-Type" "application/json; charset=utf-8"}
  :body (json/generate-string conteudo)})


(def usuario (atom ()))

(def bdGastoCalorico (atom ()))

(def bdGanhoCalorico (atom ()))

(defn registro [mapa bd]
  (swap! bd conj mapa))

(defn setUsuario [dados]
   (let [novo-usuario {:sexo (:sexo dados)
                      :idade (:idade dados)
                      :peso (:peso dados)}]
    (reset! usuario novo-usuario) novo-usuario))
 
(def tempo 0)

(defn buscar-calorias-burned [atividade]
  (client/get "https://calories-burned-by-api-ninjas.p.rapidapi.com/v1/caloriesburned"
    {:headers {"x-rapidapi-host" "calories-burned-by-api-ninjas.p.rapidapi.com"
               "x-rapidapi-key" "85b6ee325cmsh20882dff5f8b7e3p1e76fajsn7a32bbceec18"}
     :query-params {:activity atividade
                    :weight (:peso @usuario)
                    :duration tempo}}))

(defroutes app-routes
  (GET "/" [] "Hello Word")

  (GET "/cadastro" [] (como-json @usuario))
  
  (GET "/totalCalorico" [])

  (GET "/consumoCalorico" [])

  (GET "/perdaCalorica" [])
    
  (POST "/atividade" {body :body}
  (do
    (registro body bdGastoCalorico)
    (como-json {:mensagem "Atividade registrada com sucesso!" :atividade body})))

  (POST "/refeicao" [])

  (POST "/cadastro" {body :body}
    (let [usuario (setUsuario body)]
      (como-json {:usuario usuario})))

  (route/not-found "Recurso não encontrado"))

(def app
  (-> (wrap-defaults app-routes api-defaults)
      (wrap-json-body {:keywords? true :bigdecimals? true})))