package com.thoughtworks.todo

import com.thoughtworks.binding.Binding._
import com.thoughtworks.binding.dom
import scala.collection.GenSeq
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom._
import org.scalajs.dom.ext._
import org.scalajs.dom.raw._
import upickle.default._

@JSExport object Main {

  /** @note [[Todo]] is not a case class because we want to distinguish two [[Todo]]s with the same content */
  final class Todo(val title: String, val completed: Boolean)
  object Todo {
    def apply(title: String, completed: Boolean) = new Todo(title, completed)
    def unapply(todo: Todo) = Option((todo.title, todo.completed))
  }

  final case class TodoList(text: String, hash: String, items: BindingSeq[Todo])

  object Models {
    def load() = LocalStorage("todos-binding.scala") match {
      case None => Seq()
      case Some(json) => read[Seq[Todo]](json)
    }
    def save(todos: Seq[Todo]) = LocalStorage("todos-binding.scala") = write(todos)

    val allTodos = Vars[Todo](load(): _*)

    val localStorageMountPoint = new MultiMountPoint(allTodos) {
      override def set(newValue: Seq[Todo]) = save(newValue)
      override def splice(oldSeq: Seq[Todo], from: Int, that: GenSeq[Todo], replaced: Int) = {
        save(oldSeq.view(0, from) ++ that ++ oldSeq.view(from + replaced, oldSeq.length))
      }
    }
    localStorageMountPoint.watch()

    val editingTodo = Var[Option[Todo]](None)

    val all = TodoList("All", "#/", allTodos)
    val active = TodoList("Active", "#/active", for {todo <- allTodos if !todo.completed} yield todo)
    val completed = TodoList("Completed", "#/completed", for {todo <- allTodos if todo.completed} yield todo)
    val todoLists = Seq(all, active, completed)

    def getCurrentTodoList = todoLists.find(_.hash == window.location.hash).getOrElse(all)
    val currentTodoList = Var(getCurrentTodoList)
    @dom val hashBinding = window.location.hash = currentTodoList.each.hash
    hashBinding.watch()
    window.onhashchange = { _: Any => currentTodoList := getCurrentTodoList }
  }
  import Models._

  @dom def header = {
    <header class="header">
      <h1>todos</h1>
      <input class="new-todo" autofocus={true} placeholder="What needs to be done?" onkeydown={event: KeyboardEvent =>
      event.keyCode match {
        case KeyCode.Enter =>
          dom.currentTarget[HTMLInputElement].value.trim match {
            case "" =>
            case title =>
              allTodos.get += new Todo(title, false)
              dom.currentTarget[HTMLInputElement].value = ""
          }
        case _ =>
        }
      }/>
    </header>
  }

  @dom def todoListItem(todo: Todo) = {
    val suppressOnBlur = Var(false)
    def submit(newTitle: String): Unit = {
      suppressOnBlur := true
      editingTodo := None
      newTitle.trim match {
        case "" =>
          allTodos.get.remove(allTodos.get.indexOf(todo))
        case trimmedTitle =>
          allTodos.get(allTodos.get.indexOf(todo)) = new Todo(trimmedTitle, todo.completed)
      }
    }
    val edit = <input class="edit" value={ todo.title }
                      onblur={ 
                        if (suppressOnBlur.each) { _: Any =>
                        } else { _: Any =>
                          submit(dom.currentTarget[HTMLInputElement].value)
                        }
                      }
                      onkeydown={ event: KeyboardEvent =>
                        event.keyCode match {
                          case KeyCode.Escape =>
                            suppressOnBlur := true
                            editingTodo := None
                          case KeyCode.Enter =>
                            submit(dom.currentTarget[HTMLInputElement].value)
                          case _ =>
                        }
                      } />;
    <li class={s"${if (todo.completed) "completed" else ""} ${if (editingTodo.each.contains(todo)) "editing" else ""}"}>
      <div class="view">
        <input class="toggle" type="checkbox" checked={todo.completed} onclick={_: Any =>
          allTodos.get(allTodos.get.indexOf(todo)) = new Todo(todo.title, dom.currentTarget[HTMLInputElement].checked)
        }/>
        <label ondblclick={ _: Any => editingTodo := Some(todo); edit.focus() }>{ todo.title }</label>
        <button class="destroy" onclick={_: Any => allTodos.get.remove(allTodos.get.indexOf(todo))}></button>
      </div>
      { edit }
    </li>
  }

  @dom def mainSection = <section class="main" style:display={if (allTodos.length.each == 0) "none" else ""}>
    <input type="checkbox" class="toggle-all" checked={active.items.length.each == 0} onclick={_: Any =>
      for ((todo, i) <- allTodos.get.zipWithIndex) {
        if (todo.completed != dom.currentTarget[HTMLInputElement].checked) {
          allTodos.get(i) = new Todo(todo.title, dom.currentTarget[HTMLInputElement].checked)
        }
      }
    }/>
    <label htmlFor="toggle-all">Mark all as complete</label>
    <ul class="todo-list">{ for { todo <- currentTodoList.each.items } yield todoListItem(todo).each }</ul>
  </section>

  @dom def filterListItem(todoList: TodoList) = <li>
    <a href={ todoList.hash } class={ if (todoList == currentTodoList.each) "selected" else "" }>{ todoList.text }</a>
  </li>

  @dom def footer = <footer class="footer" style:display={if (allTodos.length.each == 0) "none" else ""}>
    <span class="todo-count">
      <strong>{ active.items.length.each.toString }</strong> { if (active.items.length.each == 1) "item" else "items"} left
    </span>
    <ul class="filters">{ for { todoList <- Constants(todoLists: _*) } yield filterListItem(todoList).each }</ul>
    <button class="clear-completed"
            style:visibility={if (completed.items.length.each == 0) "hidden" else "visible"}
            onclick={_: Any => allTodos.get --= (for {todo <- allTodos.get if todo.completed} yield todo) }>
      Clear completed
    </button>
  </footer>

  @dom def todoapp = {
    <section class="todoapp">{ header.each }{ mainSection.each }{ footer.each }</section>
      <footer class="info">
        <p>Double-click to edit a todo</p>
      <p>Written by <a href="https://github.com/atry">Yang Bo</a></p>
      <p>Part of <a href="http://todomvc.com">TodoMVC</a></p>
      </footer>
  }

  @JSExport def main() = dom.render(document.body, todoapp)

}
