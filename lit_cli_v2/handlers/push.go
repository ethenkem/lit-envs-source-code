package handlers

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"os"
	"time"

	"github.com/briandowns/spinner"
	"github.com/pelletier/go-toml"
	"github.com/spf13/cobra"
)

const pushAPIURL = "http://localhost:8080"
const dataFilePath = ".lit_env_data.toml"

func loadPushToken() string {
	file, err := os.Open(dataFilePath)
	if err != nil {
		return ""
	}
	defer file.Close()

	var config map[string]map[string]string
	_ = toml.NewDecoder(file).Decode(&config)
	return config["auths"]["token"]
}

func loadActiveProjectID() string {
	file, err := os.Open(dataFilePath)
	if err != nil {
		return ""
	}
	defer file.Close()

	var config map[string]map[string]string
	_ = toml.NewDecoder(file).Decode(&config)
	return config["active_project"]["id"]
}

func PushEnvFile(filePath string) {
	token := loadPushToken()
	if token == "" {
		fmt.Println("🔑 Token not found. Please login first.")
		return
	}

	projectID := loadActiveProjectID()
	if projectID == "" {
		fmt.Println("📁 Active project not found. Please select a project first.")
		return
	}

	content, err := os.ReadFile(filePath)
	if err != nil {
		fmt.Println("❌ Failed to read file:", err)
		return
	}

	payload := map[string]string{"envData": string(content)}
	jsonBody, _ := json.Marshal(payload)

	s := spinner.New(spinner.CharSets[14], 100*time.Millisecond)
	s.Suffix = " 📤 Pushing data..."
	s.Start()

	req, _ := http.NewRequest("PUT", fmt.Sprintf("%s/projects/update-env-data/%s/", pushAPIURL, projectID), bytes.NewBuffer(jsonBody))
	req.Header.Set("Authorization", "Bearer "+token)
	req.Header.Set("Content-Type", "application/json")

	client := http.Client{Timeout: 30 * time.Second}
	res, err := client.Do(req)
	s.Stop()

	if err != nil {
		fmt.Println("🚨 Network error:", err)
		return
	}
	defer res.Body.Close()

	if res.StatusCode == 200 {
		fmt.Println("✅ Push successful!")
	} else {
		fmt.Printf("❌ Push failed (%d)\n", res.StatusCode)
		body, _ := io.ReadAll(res.Body)
		fmt.Println(string(body))
	}
}

var pushCmd = &cobra.Command{
	Use:   "push",
	Short: "Push .env file to remote project",
	Run: func(cmd *cobra.Command, args []string) {
		filePath, _ := cmd.Flags().GetString("file")
		if filePath == "" {
			cmd.Println("❗ Please specify a file using --file or -f")
			return
		}
		PushEnvFile(filePath)
	},
}

func init() {
	pushCmd.Flags().StringP("file", "f", "", "Path to the .env file to push")
	rootCmd.AddCommand(pushCmd)
}
