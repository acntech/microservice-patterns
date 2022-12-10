import {FC, ReactElement, useEffect} from "react";
import {useRouter} from "next/router";

const OrderListPage: FC = (): ReactElement => {

    const router = useRouter();

    useEffect(() => {
        router.push("/")
    }, []);

    return (
        <>
        </>
    );
};

export default OrderListPage;
