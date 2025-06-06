(ns apiav3.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(def usuario (atom ()))

(defn get-usuario []
  (swap! usuario conj {:idade 30
                       :peso 70
                       :altura 1.75
                       :sexo "Masculino"})
usuario)

(defn set-usuario []
  
  
  )

(defroutes app-routes
  (GET "/" [] "Calculadora de Calorias API v3")
  (GET "/usuario" [] (get-usuario))
  (POST "/cadastro" requisicao (set-usuario (:body requisicao)))
  (route/not-found "Not Found"))
 
(def app
  (wrap-defaults app-routes site-defaults))
