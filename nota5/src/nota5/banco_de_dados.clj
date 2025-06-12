(ns nota5.banco-de-dados
  (:require [clj-http.client :as client]))

(def calorie-ninjas-key "YOUR_CALORIE_NINJAS_KEY")
(def calorie-ninjas-url "https://api.calorieninjas.com/v1/nutrition?query=")

(def rapidapi-key "85b6ee325cmsh20882dff5f8b7e3p1e76fajsn7a32bbceec18")

(def calories-burned-url "https://calories-burned-by-api-ninjas.p.rapidapi.com/v1/caloriesburned")
(def calories-burned-host "calories-burned-by-api-ninjas.p.rapidapi.com")

(def google-translate-url "https://google-translate-api14.p.rapidapi.com/translate.php")
(def google-translate-host "google-translate-api14.p.rapidapi.com")

(defn getGanhoCalorico [query]
  (client/get (str calorie-ninjas-url query)
              {:headers {"X-Api-Key" calorie-ninjas-key}}))

(defn getPerdaCalorica [activity]
  (client/get calories-burned-url
              {:headers {:x-rapidapi-key rapidapi-key
                         :x-rapidapi-host calories-burned-host}
               :query-params {:activity activity}}))

(defn getTraducao [text to-language]
  (client/get google-translate-url
              {:headers {:x-rapidapi-key rapidapi-key
                         :x-rapidapi-host google-translate-host}
               :query-params {:input_text text
                              :to_language to-language}}))

(getGanhoCalorico "3lb carrots and a chicken sandwich")
(getPerdaCalorica "skiing")
(getTraducao "how are you" "english")