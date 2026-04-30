import ProductList from "./pages/ProductList";
import "./App.css";

function App() {
    return (
        <div className="app">
            <header className="header">
                <h1>N11 Final Project</h1>
                <p>Fullstack E-Commerce Application</p>
            </header>

            <main className="container">
                <ProductList />
            </main>
        </div>
    );
}

export default App;