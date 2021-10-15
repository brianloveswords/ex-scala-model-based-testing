import main.Tree

val x = 2

var t = Tree[Int, Int]()

t = t.updated(15, 11).updated(11, 9)

t

t.get(15)
