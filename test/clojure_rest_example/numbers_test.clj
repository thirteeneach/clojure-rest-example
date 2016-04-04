(ns clojure-rest-example.numbers-test
  (:require
    [clojure.test :refer :all]
    [clojure-rest-example.numbers :refer :all]))

(deftest num-to-words-given-one-digit-number-then-words
  (is (= "zero"  (num-to-words 0)))
  (is (= "one"   (num-to-words 1)))
  (is (= "three" (num-to-words 3)))
  (is (= "nine"  (num-to-words 9))))

(deftest num-to-words-given-teen-number-then-words
  (is (= "ten"      (num-to-words 10)))
  (is (= "thirteen" (num-to-words 13)))
  (is (= "nineteen" (num-to-words 19))))

(deftest num-to-words-given-two-digit-number-then-words
  (is (= "twenty"       (num-to-words 20)))
  (is (= "twenty-three" (num-to-words 23)))
  (is (= "forty-two"    (num-to-words 42))))

(deftest num-to-words-given-three-digit-number-then-words
  (is (= "one hundred"                  (num-to-words 100)))
  (is (= "one hundred and three"        (num-to-words 103)))
  (is (= "one hundred and thirteen"     (num-to-words 113)))
  (is (= "two hundred and twenty-three" (num-to-words 223))))

(deftest num-to-words-given-four-digit-number-then-word
  (is (= "one thousand" (num-to-words 1000))))

(deftest num-to-words-given-outside-limits-then-error
  (is (thrown? AssertionError (num-to-words -1)))
  (is (thrown? AssertionError (num-to-words 1001))))


