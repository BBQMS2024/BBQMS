
export default function ExampleButton({ text, onClick }) {
    return (
        <button onClick={() => onClick()}>
            { text }
        </button>
    )
}