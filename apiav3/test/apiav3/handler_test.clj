(ns apiav3.handler-test
  (:require [midje.sweet  :refer  :all]
            [apiav3.db :as db]
            [apiav3.handler :refer [app]]
            [apiav3.defs-personalizados :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as json]
            [clj-http.client  :as  http]))

(against-background [(before :facts (iniciar-servidor porta-padrao)) (after  :facts (parar-servidor))]
                    (fact "O saldo inicial é 0" :aceitacao
                          (json/parse-string (conteudo "/saldo") true) => {:saldo 0})

                  ;;   (fact "O saldo é 10 quando a única transação é uma receita de 10" :aceitacao
                  ;;         (http/post (endereco-para "/transacoes") {:content-type :json :body (json/generate-string {:valor 10 :tipo "receita"})})
                  ;;         (json/parse-string (conteudo "/saldo") true) => {:saldo 10})

                    (fact "O	saldo	é	1000	quando	criamos	duas	receitas	de	2000e	uma	despesa	da	3000"	:aceitacao
                          (http/post	(endereco-para "/transacoes")	(receita	2000))
                          (http/post	(endereco-para "/transacoes")	(receita	2000))
                          (http/post	(endereco-para "/transacoes")	(despesa	3000))
                          (json/parse-string	(conteudo "/saldo")	true)	=>	{:saldo	1000})

                    (fact "Rejeita	uma	transação	sem	valor"	:aceitacao
                          (let	[resposta	(http/post	(endereco-para "/transacoes")
                                                    (conteudo-como-json	{:tipo
                                                                         "receita"}))]
                            (:status	resposta)	=>	422))
                    (fact "Rejeita	uma	transação	com	valor	negativo"	:aceitacao
                          (let	[resposta	(http/post	(endereco-para "/transacoes")
                                                    (receita	-100))]
                            (:status	resposta)	=>	422))
                    (fact "Rejeita	uma	transação	com	valor	que	não	é	um	número"
                          :aceitacao
                          (let	[resposta	(http/post	(endereco-para "/transacoes")
                                                    (receita "mil"))]
                            (:status	resposta)	=>	422))
                    (fact "Rejeita	uma	transação	sem	tipo"	:aceitacao
                          (let	[resposta	(http/post	(endereco-para "/transacoes")
                                                    (conteudo-como-json	{:valor	70}))]
                            (:status	resposta)	=>	422))
                    (fact "Rejeita	uma	transação	com	tipo	desconhecido"	:aceitacao
                          (let	[resposta	(http/post	(endereco-para "/transacoes")
                                                    (conteudo-como-json
                                                     {:valor	70
                                                      :tipo	"investimento"}))]
                            (:status	resposta)	=>	422)))

;;-----------------------------------------------------------------------------------------------------------------------------------------
(facts "Existe rota para lidar com filtro de transação por tipo" :unitarios
  (against-background
    [(db/transacoes-do-tipo "receita")
     => '({:id 1 :valor 2000 :tipo "receita"})
     (db/transacoes-do-tipo "despesa")
     => '({:id 2 :valor 89 :tipo "despesa"})
     (db/transacoes)
     => '({:id 1 :valor 2000 :tipo "receita"}
          {:id 2 :valor 89 :tipo "despesa"})])
  (fact "Filtro por receita" :unitarios
    (let [response (app (mock/request :get "/receitas"))]
      (:status response) => 200
      (:body response) => (json/generate-string
                            {:transacoes '({:id 1 :valor 2000 :tipo "receita"})})))
  (fact "Filtro por despesa" :unitarios
    (let [response (app (mock/request :get "/despesas"))]
      (:status response) => 200
      (:body response) => (json/generate-string
                            {:transacoes '({:id 2 :valor 89 :tipo "despesa"})})))
  (fact "Sem filtro" :unitarios
    (let [response (app (mock/request :get "/transacoes"))]
      (:status response) => 200
      (:body response) => (json/generate-string
                            {:transacoes '({:id 1 :valor 2000 :tipo "receita"}
                                           {:id 2 :valor 89 :tipo "despesa"})}))))

(facts "Saldo inicial é 0" :unitarios
       (against-background
        [(json/generate-string {:saldo 0}) => "{\"saldo\":0}"
         ;; cria mock da chamada a db/saldo
         (db/saldo) => 0])
       (let [response (app (mock/request :get "/saldo"))]
         (fact "o formato é 'application/json'" :unitarios
               (get-in response [:headers "Content-Type"])
               => "application/json; charset=utf-8")
         (fact "o status da resposta é 200" :unitarios
               (:status response) => 200)
         (fact "o texto do corpo é um JSON cuja chave é saldo e o valor é 0" :unitarios
               (:body response) => "{\"saldo\":0}")))

(facts "Registra uma receita no valor de 10"

       (against-background (db/registrar {:valor 10 :tipo "receita"}) => {:id 1 :valor 10 :tipo "receita"})

       (let [response (app (-> (mock/request :post "/transacoes")
                               (mock/json-body {:valor 10 :tipo "receita"})))]
         ;; cria o conteúdo do POST   
         (fact "o status da resposta é 201" :unitarios
               (:status response) => 201)
         (fact "o texto do corpo é um JSON com o conteúdo enviado e um id" :unitarios
               (:body response) => "{\"id\":1,\"valor\":10,\"tipo\":\"receita\"}")))