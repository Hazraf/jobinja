import React, { Component } from "react";
const axios = require("axios");
import { toast } from "react-toastify";
import ProjectInfo from "./ProjectInfo";
import Project from "../Project/Project";

export default class ProjectsList extends Component<any, State> {
  constructor(props: any) {
    super(props);
    this.state = {
      inputValue: "",
      projects: []
    };
  }
  componentDidMount() {
    var link = "http://localhost:8080/test/projects?q=";
    axios
      .get(link)
      .then((response: any) => {
        this.setDataToState(response);
      })
      .catch(function(error: any) {
        console.log(error);
        toast.error("خطا در دریافت پروژه ها");
      });
  }
  setDataToState = (response: any) => {
    console.log("response.data in setdata");
    console.log(response.data);
    var projects: any[] = [];
    if (response.data.length != 0) {
      //var key = Object.keys(response.data[0])[0];
      for (var i = 0; i < response.data.length; i++) {
        var key = Object.keys(response.data[i])[0];
        // console.log(" response.data[i].id in set data");
        // console.log(response.data[i].id);
        var link =
          "/project?id=" + response.data[i][key].id;
        var project = {
          projectId: response.data[i][key].id,
          title: response.data[i][key].title,
          description: response.data[i][key].description,
          imageURL: response.data[i][key].imageURL,
          skills: response.data[i][key].skills,
          budget: response.data[i][key].budget,
          deadline: response.data[i][key].deadline,
          linkToProPage: link
        };
        console.log("p", project);
        projects.push(project);
      }
    }
    this.setState({ projects: projects });
  };

  getProjectsInfo = () => {
    var projectJSX: JSX.Element[] = [];
    var numProjects = this.state.projects.length;
    console.log(numProjects);

    for (var i = 0; i < numProjects; i = i + 1) {
      //var key = Object.keys(this.state.projects[i])[0];
      // console.log("this.state.projects[i]");
      // console.log(this.state.projects[i].budget);
      // console.log(this.state.projects[i][key].id);
      // console.log("this.state.projects[i][key].id");
      projectJSX.push(<ProjectInfo {...this.state.projects[i]} />);
    }
    return projectJSX;
  };

  render() {
    return (
      <div>
        <div className="search-container">
          <form action="" onSubmit={e => this.submitForm(e)}>
            <button type="submit">
              <i className="" />
              جستجو
            </button>
            <input
              type="text"
              placeholder="جست و جو در جاب اونجا"
              name="search"
              onChange={e => this.handleInputChange(e)}
            />
          </form>
        </div>
        <div className="row" id="projects">
          <div className="col">{this.getProjectsInfo()}</div>
        </div>
      </div>
    );
  }

  submitForm(e: React.FormEvent<HTMLFormElement>): void {
    const { inputValue } = this.state;
    e.preventDefault();
    //toast.success(this.state.inputValue);
    //this.setState({ projects: [] });
    var link = "http://localhost:8080/test/projects?q=";
    link += this.state.inputValue;
    // console.log("searching for " + link);
    axios
      .get(link)
      .then((response: any) => {
        this.setDataToState(response);
      })
      .catch(function(error: any) {
        console.log(error);
        toast.error("خطا در دریافت پروژه ها");
      });
    //  toast.success(this.state.projectId);
  }
  handleInputChange(e: React.ChangeEvent<HTMLInputElement>): void {
    this.setState({ inputValue: e.target.value });
  }
}

interface State {
  projects: any[];
  inputValue: string;
}
