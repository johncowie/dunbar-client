(ns dunbar-client.traversy
  (:require [reagent.ratom :refer [*ratom-context* make-reaction IReactiveAtom]]
            [traversy.lens :as lens]))

(deftype RTraversy [ratom lens ^:mutable reaction]
  IAtom
  IReactiveAtom

  IEquiv
  (-equiv [o other]
    (and (instance? RTraversy other)
         (= lens (.-lens other))
         (= ratom (.-ratom other))))

  Object
  (_reaction [this]
    (if (nil? reaction)
      (set! reaction
            (if (satisfies? IDeref ratom)
              (make-reaction #(lens/view-single @ratom lens)
                             :on-set #(swap! ratom lens/update lens (lens/put %2)))
              (make-reaction #(ratom lens) ;; don't really understand how to change this bit
                             :on-set #(ratom lens %2))))
      reaction))

  (_peek [this]
    (binding [*ratom-context* nil]
      (-deref (._reaction this))))

  IDeref
  (-deref [this]
    (-deref (._reaction this)))

  IReset
  (-reset! [this new-value]
    (-reset! (._reaction this) new-value))

  ISwap
  (-swap! [a f]
    (-swap! (._reaction a) f))
  (-swap! [a f x]
    (-swap! (._reaction a) f x))
  (-swap! [a f x y]
    (-swap! (._reaction a) f x y))
  (-swap! [a f x y more]
    (-swap! (._reaction a) f x y more))

  IPrintWithWriter
  (-pr-writer [a writer opts]
    (-write writer (str "#<Traversy Lens: " lens " "))
    (pr-writer (._peek a) writer opts)
    (-write writer ">"))

  IWatchable
  (-notify-watches [this oldval newval]
    (-notify-watches (._reaction this) oldval newval))
  (-add-watch [this key f]
    (-add-watch (._reaction this) key f))
  (-remove-watch [this key]
    (-remove-watch (._reaction this) key))

  IHash
  (-hash [this] (hash [ratom lens])))

(defn traversy-cursor
  [src lens]
  (RTraversy. src lens nil))
