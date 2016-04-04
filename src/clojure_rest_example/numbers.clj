(ns clojure-rest-example.numbers
  (:require [clojure-rest-example.utils :refer [def-]]))

(def- units-names
  {0  "zero"
   1  "one"
   2  "two"
   3  "three"
   4  "four"
   5  "five"
   6  "six"
   7  "seven"
   8  "eight"
   9  "nine"
   10 "ten"
   11 "eleven"
   12 "twelve"
   13 "thirteen"
   14 "fourteen"
   15 "fifteen"
   16 "sixteen"
   17 "seventeen"
   18 "eighteen"
   19 "nineteen"})

(def- tens-names
  {10 "ten"
   20 "twenty"
   30 "thirty"
   40 "forty"
   50 "fifty"
   60 "sixty"
   70 "seventy"
   80 "eighty"
   90 "ninety"})

(defn- convert-less-than-one-hundred
  [num]
  (let [tens  (quot num 10)
        units (rem num 10)]
    (if (< num 20)
      (get units-names num)
      (str (get tens-names (* 10 tens))
           (when (pos? units) (str "-" (get units-names units)))))))

(defn- convert-less-than-one-thousand [num]
  (let [hundreds  (quot num 100)
        remainder (rem num 100)]
    (str
     (when (pos? hundreds) (str (convert-less-than-one-hundred hundreds) " hundred"))
     (when (and (pos? hundreds) (pos? remainder)) " and ")
     (when (pos? remainder) (convert-less-than-one-hundred remainder)))))

(defn num-to-words
  "Returns num as a string of English words, where num is a number
  between zero and one thousand inclusive."
  [num]
  {:pre [(<= 0 num 1000)]}
  (if (zero? num)
    "zero"
    (let [thousands (quot num 1000)
          remainder (rem num 1000)]
      (str
       (when (pos? thousands) (str (num-to-words thousands) " thousand"))
       (when (and (pos? thousands) (pos? remainder)) " and ")
       (when (pos? remainder) (convert-less-than-one-thousand remainder))))))

