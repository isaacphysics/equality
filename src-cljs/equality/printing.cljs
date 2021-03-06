(ns equality.printing)

(defmulti expr-str :type)

(defmethod expr-str :default [expr]
  (str "_" (:token expr)))

(defmethod expr-str :type/num [expr]
  (str (if (:certain (meta expr)) "@" "") (:token expr)))

(defmethod expr-str :type/var [expr]
  (str (if (:certain (meta expr)) "@" "") (:token expr)))

(defmethod expr-str :type/pow [expr]
  (str (if (:certain (meta expr)) "@" "") (expr-str (:base expr)) "^[" (expr-str (:exponent expr)) "]"))

(defmethod expr-str :type/add [expr]
  (str (if (:certain (meta expr)) "@" "") (expr-str (:left-op expr)) " + " (expr-str (:right-op expr))))

(defmethod expr-str :type/sub [expr]
  (str (if (:certain (meta expr)) "@" "") (expr-str (:left-op expr)) " - " (expr-str (:right-op expr))))

(defmethod expr-str :type/mult [expr]
  (str (if (:certain (meta expr)) "@" "") "[" (expr-str (:left-op expr)) (expr-str (:right-op expr)) "]"))

(defmethod expr-str :type/eq [expr]
  (str (if (:certain (meta expr)) "@" "") (expr-str (:left-op expr)) " = " (expr-str (:right-op expr))))

(defmethod expr-str :type/frac [expr]
  (str (if (:certain (meta expr)) "@" "")  "[" (expr-str (:numerator expr)) "] / [" (expr-str (:denominator expr)) "]"))

(defmethod expr-str :type/bracket [expr]
  (str (if (:certain (meta expr)) "@" "") "(" (expr-str (:child expr)) ")"))

(defn print-expr [expr]
  (js/console.log (expr-str expr)))

(defmulti mathml-inner :type)

(defmethod mathml-inner nil [expr]
  "")

(defmethod mathml-inner :type/symbol [expr]
  (println "Should not have called mathml with expr of type symbol:" expr)
  "")

(defmethod mathml-inner :type/var [expr]
  (str "<mi id=\"" (:id expr) "\">" (:token expr) "</mi>"))

(defmethod mathml-inner :type/num [expr]
  (str "<mn id=\"" (:id expr) "\">" (:token expr) "</mn>"))

(defmethod mathml-inner :type/pow [expr]
  (str "<mrow><msup>" (mathml-inner (:base expr)) (mathml-inner (:exponent expr)) "</msup></mrow>"))

(defmethod mathml-inner :type/add [expr]
  (str "<mrow>" (mathml-inner (:left-op expr)) "<mo id=\"" (:id expr) "\">+</mo>" (mathml-inner (:right-op expr)) "</mrow>"))

(defmethod mathml-inner :type/sub [expr]
  (str "<mrow>" (mathml-inner (:left-op expr)) "<mo id=\"" (:id expr) "\">-</mo>" (mathml-inner (:right-op expr)) "</mrow>"))

(defmethod mathml-inner :type/mult [expr]
  (str "<mrow>" (mathml-inner (:left-op expr)) (mathml-inner (:right-op expr)) "</mrow>"))

(defmethod mathml-inner :type/eq [expr]
  (str "<mrow>" (mathml-inner (:left-op expr)) "<mo id=\"" (:id expr) "\">=</mo>" (mathml-inner (:right-op expr)) "</mrow>"))

(defmethod mathml-inner :type/frac [expr]
  (str "<mfrac id=\"" (:id expr) "\"><mrow>" (mathml-inner (:numerator expr)) "</mrow><mrow>" (mathml-inner (:denominator expr)) "</mrow></mfrac>"))

(defmethod mathml-inner :type/sqrt [expr]
  (str "<msqrt id=\"" (:id expr) "\">" (mathml-inner (:radicand expr)) "</msqrt>"))

(defmethod mathml-inner :type/bracket [expr]
  (str "<mfenced id=\"" (:id expr) "\"><mrow>" (mathml-inner (:child expr)) "</mrow></mfenced>"))

(defn mathml [expr]
  (when expr
    (str "<math display=\"block\"><mrow>" (mathml-inner expr) "</mrow></math>")))
