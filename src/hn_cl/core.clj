(ns hn-cl.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [morse.handlers :as handlers]
            [morse.api :as t]
            )
  )
(require '[morse.polling :as p])

;; Create token.data with you bot secret token in it
(def token (slurp "token.data"))
(def topstories-url "https://hacker-news.firebaseio.com/v0/topstories.json")
(def item-url "https://hacker-news.firebaseio.com/v0/item/%d.json")

(defn top-stories-ids [] 
  (json/read-str (slurp topstories-url)))

(defn story-content [id]
  (json/read-str (slurp (str (format item-url id)))))

(defn format-story [id]
  (def story (story-content id))
  (str
   "[" (get story "score") "]" " " (get story "title") " " (get story "url") "\n\n"))

(defn top-stories-content []
  (apply str (for [id (take 10 (top-stories-ids))] (format-story id))))


(handlers/defhandler bot-api
  (handlers/command-fn "start" (fn [{{id :id :as chat} :chat}]
                          (t/send-text token id "Type /new to sidplay latest topics")))

  (handlers/command-fn "new" (fn [{{id :id :as chat} :chat}]
                                   (println "Bot joined new chat: " chat)
                                   (t/send-text token id (top-stories-content))))
  )

(defn -main
  "Print top stories from hackernews"
  [& args]
  (p/start token bot-api)
  (Thread/sleep Long/MAX_VALUE) 
  )
