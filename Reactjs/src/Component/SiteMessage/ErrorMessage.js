

export default function ErrorMessage({message}) {
        return (
            <div className="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>Error!</strong> {message}

                <button
                    type="button"
                    className="btn-close"
                    aria-label="Close"
                    />
            </div>
        )
}

