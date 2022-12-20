(ns hn-cl.core
  (:gen-class)
  (:require [clojure.data.json :as json])
  (:use [clojure.pprint :only [pprint]]))

(def topstories-url "https://hacker-news.firebaseio.com/v0/topstories.json")
(def item-url "https://hacker-news.firebaseio.com/v0/item/%d.json")

(defn top-stories-ids [] 
  (json/read-str (slurp topstories-url)))

(defn story-content [id]
  (json/read-str (slurp (str (format item-url id)))))

(defn print-story [id] 
  (def story (story-content id))
  (println (str 
            "[" (get story "score") "]" " " (get story "title") "\n"
            "> " (get story "url")
            )))

(defn -main
  "Print top stories from hackernews"
  [& args]
   (doseq [id (take 10 (top-stories-ids))] (print-story id)))
