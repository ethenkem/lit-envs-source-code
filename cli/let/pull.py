import requests
import toml
import yaml
from pathlib import Path
from halo import Halo
import click

DATA_PATH = Path(".data.toml")
ENV_PATH = Path(".env")
CONFIG_PATH = Path("./let/config.yaml")


def load_token():
    if DATA_PATH.exists():
        data = toml.load(DATA_PATH)
        return data.get("auths", {}).get("token")
    return None


def load_project_id():
    if DATA_PATH.exists():
        data = toml.load(DATA_PATH)
        return data.get("active_project", {}).get("id")
    return None


def load_api_url():
    if CONFIG_PATH.exists():
        with open(CONFIG_PATH, "r") as f:
            config = yaml.safe_load(f)
            return config.get("server", {}).get("api_url")
    return None


@click.command()
def pull():
    token = load_token()
    project_id = load_project_id()
    api_url = load_api_url()

    if not token or not project_id or not api_url:
        click.secho("❌ Missing token, project ID, or API URL.", fg="red")
        return

    spinner = Halo(text="Fetching project .env data...", spinner="dots")
    spinner.start()

    try:
        response = requests.get(
            f"{api_url}/projects/pull-env-data/{project_id}",
            headers={"Authorization": f"Bearer {token}"},
            timeout=30,
        )
        spinner.stop()

        if response.status_code != 200:
            click.secho(
                f"❌ Failed to fetch project: {response.status_code}",
                fg="red",
            )
            click.secho(response.text, fg="red")
            return

        env_content = response.json().get("data", "")

        if not env_content.strip():
            click.secho(
                "⚠️ No environment data found for this project.",
                fg="yellow",
            )
            return

        with open(ENV_PATH, "w") as f:
            f.write(env_content)

        click.secho(
            f"✅ Environment variables written to {ENV_PATH}",
            fg="green",
        )

    except requests.RequestException as e:
        spinner.fail("🚨 Network error.")
        click.secho(str(e), fg="red")
