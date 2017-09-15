(require '[cljs.build.api :as b])

(b/watch "src"
  {:main 'hello-cljs.core
   :output-to "out/hello_cljs.js"
   :output-dir "out"})
