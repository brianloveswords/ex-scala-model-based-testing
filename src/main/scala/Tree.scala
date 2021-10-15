package main

import scala.annotation.tailrec

case class Tree[K, V](root: Option[Node[K, V]] = None)(using ord: Ordering[K]):
  def updated(key: K, value: V) =
    // not tail recursive/stack safe :(
    def go(n: Option[Node[K, V]]): Node[K, V] = n match
      case Some(node) =>
        if node.key == key then node.copy(value = value)
        else if ord.gt(node.key, key) then node.copy(left = Some(go(node.left)))
        else node.copy(right = Some(go(node.right)))
      case None => Node(key, value)

    Tree(Some(go(root)))

  def get(key: K): Option[V] =
    @tailrec
    def go(curr: Option[Node[K, V]]): Option[V] =
      curr match
        case Some(node) =>
          if node.key == key then Some(node.value)
          else if ord.gt(node.key, key) then go(node.left)
          else go(node.right)
        case None => None
    go(root)

case class Node[K, V](
    key: K,
    value: V,
    left: Option[Node[K, V]] = None,
    right: Option[Node[K, V]] = None
)
