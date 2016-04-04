(ns clojure-rest-example.rest-test
  (:require [cheshire.core :refer [generate-string]]
            [clojure
             [test :refer :all]
             [walk :refer [keywordize-keys]]]
            [clojure-rest-example.rest :refer :all]
            [ring.mock.request :refer [body content-type header request]]
            )
  (:import javax.servlet.http.HttpServletResponse))

(def translate-url "http://localhost:3000/translate")

(defn- app-post [url data]
  (keywordize-keys
   (app (-> (request :post url)
            (body (generate-string data))
            (content-type "application/json")
            (header "Accept" "application/json")))))

(deftest translate-given-invalid-parameter-then-bad-request
  (is (= HttpServletResponse/SC_BAD_REQUEST (:status (app-post translate-url {:number "invalid-number"}))))
  (is (= HttpServletResponse/SC_BAD_REQUEST (:status (app-post translate-url {}))))
  (is (= HttpServletResponse/SC_BAD_REQUEST (:status (app-post translate-url {:number "-1"}))))
  (is (= HttpServletResponse/SC_BAD_REQUEST (:status (app-post translate-url {:number "1001"})))))

(deftest translate-given-valid-parameter-then-sets-headers-and-response
  (let [response (app-post translate-url {:number "23"})]
    (is (= HttpServletResponse/SC_OK (response :status)))
    (is (= "text/plain" (get-in response [:headers :Content-Type])))))

(deftest translate-given-valid-parameter-then-translates
  (is (= "zero"                         (:body (app-post translate-url {:number "0"}))))
  (is (= "twenty-three"                 (:body (app-post translate-url {:number "23"}))))
  (is (= "one hundred and twenty-three" (:body (app-post translate-url {:number "123"})))))
