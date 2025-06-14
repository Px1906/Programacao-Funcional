(ns apiav3.filtros-test
  (:require 	[midje.sweet	:refer	:all]
             [cheshire.core	:as	json]
             [clj-http.client	:as	http]
             [apiav3.db	:as	db]
             [apiav3.defs-personalizados :refer :all]))

(def	transacoes-aleatorias
  '({:valor	7.0M	:tipo	"despesa"}
    {:valor	88.0M	:tipo	"despesa"}
    {:valor	106.0M	:tipo	"despesa"}
    {:valor	8000.0M	:tipo	"receita"}))

(against-background	[(before :facts [(iniciar-servidor	porta-padrao) (db/limpar)])
                     (after	:facts	(parar-servidor))]

(fact "Não	existem	receitas"	:aceitacao
      (json/parse-string	(conteudo "/receitas")	true)
      =>	{:transacoes	'()})
(fact "Não	existem	despesas"	:aceitacao
      (json/parse-string	(conteudo "/despesas")	true)
      =>	{:transacoes	'()})
(fact "Não	existem	transacoes"	:aceitacao
      (json/parse-string	(conteudo "/transacoes")	true)
      =>	{:transacoes	'()})

(against-background
  [(before	:facts	(doseq	[transacao	transacoes-aleatorias]
                    (db/registrar	transacao)))
   (after	:facts	(db/limpar))]
  (fact "Existem	3	despesas"	:aceitacao
        (count	(:transacoes	(json/parse-string
                             (conteudo "/despesas")	true)))	=>	3)
  (fact "Existe	1	receita"	:aceitacao
        (count	(:transacoes	(json/parse-string
                             (conteudo "/receitas")	true)))	=>	1)
  (fact "Existem	4	transações"	:aceitacao
        (count	(:transacoes	(json/parse-string
                             (conteudo "/transacoes")	true)))	=>	4)))
