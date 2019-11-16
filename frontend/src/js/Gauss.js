import React, { Component } from 'react'
import { Table, Row, Col, Card, CardTitle, TextInput, Collection, CollectionItem } from 'react-materialize';
import HighlightedButton from './HighlightedButton';
import { updateAuth } from './actions';
import { bindActionCreators } from 'redux';
import { calculate, getEmitter } from './services/gaussService';
import { alertError, alertSuccess } from './services/alertService';
import Alert from 'react-s-alert';

import { connect } from 'react-redux';

class Login extends Component {
    constructor(props) {
        super(props);
        this.handleChange = this.handleChange.bind(this);
        this.handleChangeTotalVariables = this.handleChangeTotalVariables.bind(this);
        this.handleChangeLinearSystem = this.handleChangeLinearSystem.bind(this);
        this.renderLinearSystem = this.renderLinearSystem.bind(this);
        this.renderLinearSystemHeader = this.renderLinearSystemHeader.bind(this);
        this.renderLinearSystemLine = this.renderLinearSystemLine.bind(this);
        this.calculateGauss = this.calculateGauss.bind(this)
        this.renderList = this.renderList.bind(this);

    }

    state = {
        results: [],
        params: {
            matriz: [[10, 2, 1, 7], [1, 5, 1, -8], [2, 3, 10, 6]],
            maxLoop: 10,
            computerError: 0.0001,
            totalVariables: 3,
            millisecondsInterval: 100
        }
    }

    handleChange = evt => {
        this.setState({ params: { ...this.state.params, [evt.target.name]: evt.target.value } });
    }
    handleChangeTotalVariables = evt => {
        const variables = new Number(evt.target.value)
        let newMatriz = []
        for (let i = 0; i < variables; i++) {
            let pos = []
            for (let e = 0; e <= variables; e++) {
                pos.push(1)
            }
            newMatriz[i] = pos
        }
        this.setState({ params: { ...this.state.params, matriz: newMatriz, [evt.target.name]: new Number(evt.target.value) } });
    }

    calculateGauss() {
        this.setState({ results: [] });
        calculate(this.state.params)
            .then(
                (result) => {
                    let source = getEmitter(result)
                    alertSuccess('Calculando...')
                    source.onmessage = (event) => {
                        if (this.state.results.length % 20 === 0 ) {
                            this.setState({ results: [] });
                        }
                        this.setState(state => {
                            const list = state.results
                            list.push(event.data);
                            return {
                                list,
                                value: '',
                            };
                        });
                    }
                    source.onerror = (event) => {
                        // alertError('Erro ao calcular!')
                        source.close();
                    }
                },
                (error) => {
                    const error2 = error && error.response && error.response.data && error.response.data.message
                    alertError('Erro ao calcular', error2)
                }
            )
    }

    renderList(row, index) {
        return (<CollectionItem href="javascript:;" >
            {row}
        </CollectionItem>)
    }

    renderLinearSystem(row, index) {
        return (<tr href="javascript:;">
            {row.map((r, i) => this.renderLinearSystemLine(r, i, index))}
        </tr>)
    }

    renderLinearSystemHeader(row, index) {
        const isFinishLine = (index) == this.state.params.totalVariables - 1
        return (
            <th>{"X" + (index + 1) + (isFinishLine ? " = " : " + ")}  </th>
        )
    }
    renderLinearSystemLine(row, index, line) {
        const isFinishLine = (index) == this.state.params.totalVariables

        return (<td>
            <TextInput name='value' value={this.state.params.matriz[line][index]}
                onChange={(evt) => this.handleChangeLinearSystem(line, index, evt.target.value)}
                label={isFinishLine ? "=" : ""}

            /></td>)
    }

    handleChangeLinearSystem(line, column, value) {
        const list = this.state.params.matriz;
        list[line][column] = value
        this.setState({ params: { ...this.state.params, matriz: list } });
    }

    render() {
        return (
            <div class="login">
                <Row>

                    <Col m={12} s={12}>
                        <form>
                            <Card header={<CardTitle />}
                                actions={[
                                    <Col m={12} s={12}>
                                        <Col m={6} s={6}><HighlightedButton onClick={this.calculateGauss} text="Calcular"></HighlightedButton></Col>
                                    </Col>,
                                    <br></br>
                                ]}
                                title="Configuração" >
                                <div class="makerspace-detail__form-content" >
                                    <Alert stack={{ limit: 2 }} />
                                    <hr></hr>
                                    <Row>
                                        <Col m={12} s={12}>
                                            <TextInput m={3} s={3} type="number" name='maxLoop' value={this.state.params.maxLoop} onChange={this.handleChange} label="Nº de interações: " />
                                            <TextInput m={3} s={3} type="number" name='computerError' value={this.state.params.computerError} onChange={this.handleChange} label="Erro computacional:" />
                                            <TextInput m={3} s={3} type="number" name='millisecondsInterval' value={this.state.params.millisecondsInterval} onChange={this.handleChange} label="Intervalo em milisegundos:" />
                                            <TextInput m={3} s={3} type="number" name='totalVariables' value={this.state.params.totalVariables} onChange={this.handleChangeTotalVariables} label="Nº de variáveis:" />
                                        </Col>
                                        <Col m={12} s={12}>

                                            <Table>
                                                <thead>
                                                    <tr>
                                                        {this.state.params.matriz.map(this.renderLinearSystemHeader)}
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    {this.state.params.matriz.map(this.renderLinearSystem)}
                                                </tbody>
                                            </Table>
                                        </Col>
                                    </Row>
                                </div>
                            </Card>
                        </form>
                    </Col>
                    <Col m={12} s={12}>
                        <Card header={<CardTitle />}
                            title="Iterações" >
                            <hr></hr>
                            <Collection>
                                {this.state.results.map(this.renderList)}
                            </Collection>
                        </Card>
                    </Col>
                </Row>
            </div>
        );
    }
}

const mapStateToProps = store => ({
    auth: store.authState.auth,
});
const mapDispatchToProps = dispatch =>
    bindActionCreators({ updateAuth }, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(Login);