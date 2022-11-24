import React, {FC, ReactElement} from 'react'
import { FormattedMessage } from 'react-intl';
import { Link } from 'react-router-dom';
import { Breadcrumb } from 'semantic-ui-react';

export interface BreadcrumbMenuItem {
    message: string;
    link?: string;
    active: boolean;
}

interface FragmentProps {
    menuItems: BreadcrumbMenuItem[];
}

export const BreadcrumbMenu: FC<FragmentProps> = (props: FragmentProps): ReactElement => {
    const { menuItems } = props;

    const breadcrumbSections: ReactElement[] = [];
    menuItems.forEach((menuItem, index) => {
        const { message, link, active } = menuItem;
        if (link) {
            breadcrumbSections.push(
                <Breadcrumb.Section key={`breadcrumb-section-${index}`} active={active}>
                    <Link to={link}><FormattedMessage id={message} /></Link>
                </Breadcrumb.Section>
            );
        } else {
            breadcrumbSections.push(
                <Breadcrumb.Section key={`breadcrumb-section-${index}`} active={active}>
                    <FormattedMessage id={message} />
                </Breadcrumb.Section>
            );
        }
        breadcrumbSections.push(<Breadcrumb.Divider key={`breadcrumb-divider-${index}`} icon="right chevron" />);
    });
    breadcrumbSections.pop();

    return (
        <Breadcrumb>{breadcrumbSections}</Breadcrumb>
    );
};