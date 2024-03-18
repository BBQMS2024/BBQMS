import Home from './pages/Home/Home'
import { Route, Routes } from 'react-router-dom'


/* 
    Kratki opis kako bi preporucio da radite jer vas je vise pa da ima neke strukture
    Imamo folder "pages" i "components", imena su prilicno opisna, odavde samo dodajete rute na page-eve, koji se sastoje od komponenti
    Components su one stvari koje se cesto ponavljaju, npr imate isti button na vise mjesta, isti container na vise mjesta itd.
    Za guide o reactu mozete pogledati: https://react.dev/learn
    Za guide o routanju mozete pogledati: https://hygraph.com/blog/routing-in-react

    Ovo gore su neke preporuke o strukturi iz mog iskustva, ako vam se ne svidja mozete i drugacije organizovati
    Ako budete imali pitanja o React ili nesto sl. mozete se obratiti ekapic
*/
export default function App() {
    return (
        <>
            <Routes>
                <Route path='/' element={ <Home /> } />
            </Routes>
        </>
    )
}
