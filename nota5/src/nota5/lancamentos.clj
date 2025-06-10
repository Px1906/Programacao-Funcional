(ns nota5.lancamentos) 

;; Função que valida um lançamento financeiro.
(defn valida? [lancamento]
  (and
   (contains? lancamento :valor)
   (number? (:valor lancamento))
   (pos? (:valor lancamento))
   (contains? lancamento :tipo)
   (or
    (= "despesa" (:tipo lancamento))
    (= "receita" (:tipo lancamento)))))
