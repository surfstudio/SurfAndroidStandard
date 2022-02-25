# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Warn when there is a big PR
warn("Big PR") if git.lines_of_code > 500

# Warn when there is mapping.txt file
warn "This PR contains mapping.txt :disappointed:" if git.added_files.include? "mapping.txt"
warn "This PR contains mapping.txt :disappointed:" if git.modified_files.include? "mapping.txt"

Dir["*/build/reports/detekt/detekt-checkstyle.xml"].each do |file_name|
  kotlin_detekt.severity = "warning"
  kotlin_detekt.filtering = true
  kotlin_detekt.skip_gradle_task = true
  kotlin_detekt.report_file = file_name
  kotlin_detekt.detekt(inline_mode: true)
end

the_coding_love.random