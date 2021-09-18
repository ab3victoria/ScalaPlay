if (window.console) {
  console.log("Running Expenses List App");
}

const ce = React.createElement
const csrfToken = document.getElementById("csrfToken").value;
const validateRoute = document.getElementById("validateRoute").value;
const expensesRoute = document.getElementById("expensesRoute").value;
const createRoute = document.getElementById("createRoute").value;
const deleteRoute = document.getElementById("deleteRoute").value;
const addRoute = document.getElementById("addRoute").value;
const logoutRoute = document.getElementById("logoutRoute").value;

class MainComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = { loggedIn: false };
  }

  render() {
    if (this.state.loggedIn) {
      return ce(ExpensesListComponent, { doLogout: () => this.setState( { loggedIn: false})});
    } else {
      return ce(LoginComponent, { doLogin: () => this.setState( { loggedIn: true }) });
    }
  }
}

class LoginComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loginName: "",
      loginPass: "",
      createName: "",
      createPass: "",
      loginMessage: "",
      createMessage: ""
    };
  }

  render() {
    return ce('div', null,
        ce('h2', null, 'Login:'),
        ce('br'),
        'Username: ',
        ce('input', {type: "text", id: "loginName", value: this.state.loginName, onChange: e => this.changerHandler(e)}),
        ce('br'),
        'Password: ',
        ce('input', {type: "password", id: "loginPass", value: this.state.loginPass, onChange: e => this.changerHandler(e)}),
        ce('br'),
        ce('button', {onClick: e => this.login(e)}, 'Login'),
        ce('span', {id: "login-message"}, this.state.loginMessage),
        ce('h2', null, 'Create User:'),
        ce('br'),
        'Username: ',
        ce('input', {type: "text", id: "createName", value: this.state.createName, onChange: e => this.changerHandler(e)}),
        ce('br'),
        'Password: ',
        ce('input', {type: "password", id: "createPass", value: this.state.createPass, onChange: e => this.changerHandler(e)}),
        ce('br'),
        ce('button', {onClick: e => this.createUser(e)}, 'Create User'),
        ce('span', {id: "create-message"}, this.state.createMessage)
    );
  }

  changerHandler(e) {
    this.setState({ [e.target['id']]: e.target.value });
  }

  login(e) {
    const username = this.state.loginName;
    const password = this.state.loginPass;
    fetch(validateRoute, {
      method: 'POST',
      headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
      body: JSON.stringify({ username, password })
    }).then(res => res.json()).then(data => {
      if(data) {
        this.props.doLogin();
      } else {
        this.setState({ loginMessage: "Login Failed" });
      }
    });
  }

  createUser() {
    const username = this.state.createName;
    const password = this.state.createPass;
    fetch(createRoute, {
      method: 'POST',
      headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
      body: JSON.stringify({ username, password })
    }).then(res => res.json()).then(data => {
      if(data) {
        this.props.doLogin();
      } else {
        this.setState({ createMessage: "User Creation Failed"});
      }
    });
  }
}

class ExpensesListComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = { expenses: [], newExpense: "", expenseMessage: "" };
  }

  componentDidMount() {
    this.loadExpenses();
  }

  render() {
    return ce('div', null,
        'Expense List',
        ce('br'),
        ce('ul', null,
            this.state.expenses.map(expense => ce('li', { key: expense.id, onClick: e => this.handleDeleteClick(expense.id) }, expense.text))
        ),
        ce('br'),
        ce('div', null,
            ce('input', {type: 'text', value: this.state.newExpense, onChange: e => this.handleChange(e) }),
            ce('button', {onClick: e => this.handleAddClick(e)}, 'Add Expense'),
            this.state.expenseMessage
        ),
        ce('br'),
        ce('button', { onClick: e => this.props.doLogout() }, 'Log out')
    );
  }

  loadExpenses() {
    fetch(expensesRoute).then(res => res.json()).then(tasks => this.setState({ tasks }));
  }

  handleChange(e) {
    this.setState({newExpense: e.target.value})
  }

  handleAddClick(e) {
    fetch(addRoute, {
      method: 'POST',
      headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
      body: JSON.stringify(this.state.newExpense)
    }).then(res => res.json()).then(data => {
      if(data) {
        this.loadExpenses();
        this.setState({ expenseMessage: "", newExpense: "" });
      } else {
        this.setState({ expenseMessage: "Failed to add." });
      }
    });
  }

  handleDeleteClick(i) {
    fetch(deleteRoute, {
      method: 'POST',
      headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
      body: JSON.stringify(i)
    }).then(res => res.json()).then(data => {
      if(data) {
        this.loadExpenses();
        this.setState({ expenseMessage: "" });
      } else {
        this.setState({ expenseMessage: "Failed to delete."});
      }
    });
  }
}

ReactDOM.render(
    ce(MainComponent, null, null),
    document.getElementById('react-root')
);
