#!/usr/bin/env bb

;; A small Babashka script for generating
;; phonewords for given (phone) numbers.

;; Usage:

;; To generate all combinations that contain any (common) English words:
;; echo 43556 | bb -o phonewords.clj

;; To generate combinations that contain exact words:
;; echo 43556 | bb -o phonewords.clj
;;
;; Generate all combinations from numbers in a file:
;; cat numbers.txt | bb -o phonewords.clj -s

;; The script can take a specific EDN dictionary file:
;; echo 937286 | bb -o phonewords.clj -d special-dictionary.edn

;; Copyright (c) 2023 Dejan Josifović, the paranoid times

;; Permission is hereby granted, free of charge, to any person obtaining
;; a copy of this software and associated documentation files (the "Software"),
;; to deal in the Software without restriction, including without limitation the
;; rights to use, copy, modify, merge, publish, distribute, sublicense,
;; and/or sell copies of the Software, and to permit persons to whom the Software
;; is furnished to do so, subject to the following conditions:

;; The above copyright notice and this permission notice shall be included
;; in all copies or substantial portions of the Software.

;; THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
;; INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
;; AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
;; DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
;; OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

(ns phonewords
  (:require [clojure.string :as st :refer [split includes?]]
            [clojure.edn :as edn :refer [read]]
            [clojure.tools.cli :as cli :refer [parse-opts]]
            [clojure.java.io :as io :refer [reader]]))

(def character-set {"0" ["0"]
                    "1" ["1"]
                    "2" ["A" "B" "C"]
                    "3" ["D" "E" "F"]
                    "4" ["G" "H" "I"]
                    "5" ["J" "K" "L"]
                    "6" ["M" "N" "O"]
                    "7" ["P" "Q" "R" "S"]
                    "8" ["T" "U" "V"]
                    "9" ["W" "X" "Y" "Z"]})

(def cli-options
  [["-s" "--strict-match" "Specifies if to search only for exact matches"
    :default false]
   ["-d" "--dictionary-file DICT" "Dictionary file"
    :default "common-english-words.edn"
    :validate [#(not (empty %)) "Dictionary file name should not be empty!"]]])

(defn read-dictionary-edn
  [f-name]
  (with-open [r (io/reader f-name)]
    (edn/read (java.io.PushbackReader. r))))

(defn cartesian-product
  [colls]
  (if (empty? colls)
    '(())
    (for [more (cartesian-product (rest colls))
          x (first colls)]
      (cons x more))))

(defn split-numbers
  [nums]
  (filter #(re-matches #"[0-9]" %)
          (st/split nums #"")))

(defn match-fn
  [exact dict phrase]
  (let [mfn (if exact
              =
              st/includes?)]
    (not (empty? (filter #(mfn phrase %) dict)))))

(let [opts (-> *command-line-args*
               (cli/parse-opts cli-options)
               :options)
      dictionary (-> opts
                     :dictionary-file
                     read-dictionary-edn)
      strict (-> opts :strict-match)]
  (for [l (line-seq (io/reader *in*))]
    (filter (partial match-fn strict dictionary)
            (map (partial apply str) (cartesian-product
                                      (map character-set (split-numbers l)))))))
