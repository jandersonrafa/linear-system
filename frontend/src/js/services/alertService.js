import Alert from 'react-s-alert';
import 'react-s-alert/dist/s-alert-default.css';
import 'react-s-alert/dist/s-alert-css-effects/genie.css';

export const alertError = (title, error) => {
  const msgError = error && error.response && error.response.data.type && error.response.data.type == 'validation' ? '<ul>' + error.response.data.errors.map(error => '<li>' + error + '</li>').join('') + '</ul>' : error;
  Alert.error('<h6><b>' + title + '</b></h6>' + msgError, {
    position: 'bottom-right',
    effect: 'genie',
    html: true,
    timeout: 5000
  });
}

export const alertSuccess = (title, options) => {
  const defaultOptions = {
    position: 'bottom-right',
    effect: 'genie',
    html: true,
    timeout: 4000
  } 
  Alert.success(title ? title : 'Registro salvo com sucesso! Você será redirecionado para o login.', {...defaultOptions, ...options });
}
export const alertWarning = (title, options) => {
  const defaultOptions = {
    position: 'bottom-right',
    effect: 'genie',
    html: true,
    timeout: 4000
  } 
  Alert.warning(title, {...defaultOptions, ...options });
}