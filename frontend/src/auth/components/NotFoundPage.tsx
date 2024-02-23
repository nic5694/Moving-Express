import React from 'react';
import {Link} from "react-router-dom";

const NotFoundPage: React.FC = () => {


    return (

        <div className={"flex flex-col align-middle h-[100vh] text-center justify-center"}>
                <h1 style={styles.heading}>404 Not Found</h1>
                <p style={styles.paragraph}>
                    Sorry, the page you are looking for could not be found.
                </p>
                <p style={styles.paragraph}>
                    Return to <Link className={"font-bold"} to={"/"}>landing page</Link>.
                </p>
        </div>
    );
};

const styles = {
    heading: {
        fontSize: '3em',
        marginBottom: '10px',
    },
    paragraph: {
        fontSize: '1.2em',
    },
};

export default NotFoundPage;
