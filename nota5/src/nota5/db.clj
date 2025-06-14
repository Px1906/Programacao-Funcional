(ns nota5.db)
;;Falta ATUALIZAR
(def	registros
  (atom	[]))

(defn	transacoes	[]
  @registros)

(defn	registrar	[transacao]
  (let	[colecao-atualizada	(swap!	registros	conj	transacao)]
    (merge	transacao	{:id	(count	colecao-atualizada)})))

(defn	limpar	[]
  (reset!	registros	[]))

(defn-	despesa?	[transacao]
  (=	(:tipo	transacao)	"despesa"))

(defn-	calcular	[acumulado	transacao]
  (let	[valor	(:valor	transacao)]
    (if	(despesa?	transacao)
      (-	acumulado	valor)
      (+	acumulado	valor))))

(defn	saldo	[]
  (reduce	calcular	0	@registros))

(defn	transacoes-do-tipo	[tipo]
  '())